package gregtech.api.util.async;

import lombok.val;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UrgentExecutor {
    private static final int PARALLELISM = Runtime.getRuntime().availableProcessors();
    private static final UrgentAsyncTaskExecutor[] EXECUTORS = new UrgentAsyncTaskExecutor[PARALLELISM - 1];
    static {
        for (int i = 0; i < PARALLELISM - 1; i++) {
            EXECUTORS[i] = new UrgentAsyncTaskExecutor();
        }
    }

    private static List<Future<?>> segmentedDispatch(int size, SegmentedRunnable segmentedTask) {
        int stride = Math.max(64, size / PARALLELISM);
        val work = new ArrayList<Future<?>>(PARALLELISM);
        int worker = 0;
        for (int start = 0; start < size; start += stride) {
            int end = Math.min(size, start + stride);
            if (end == size || worker == PARALLELISM - 1) {
                segmentedTask.run(start, size);
                break;
            } else {
                int finalStart = start;
                work.add(EXECUTORS[worker++].submit(() -> segmentedTask.run(finalStart, end)));
            }
        }
        return work;
    }
    private static <T> List<Future<T>> segmentedDispatch(int size, SegmentedSupplier<T> segmentedTask) {
        int stride = Math.max(64, size / PARALLELISM);
        val work = new ArrayList<Future<T>>(PARALLELISM);
        int worker = 0;
        for (int start = 0; start < size; start += stride) {
            int end = Math.min(size, start + stride);
            if (end == size || worker == PARALLELISM - 1) {
                work.add(CompletableFuture.completedFuture(segmentedTask.get(start, size)));
                break;
            } else {
                int finalStart = start;
                work.add(EXECUTORS[worker++].submit(() -> segmentedTask.get(finalStart, end)));
            }
        }
        return work;
    }
    private static <T> List<T> resolveWork(List<Future<T>> work) throws ExecutionException, InterruptedException {
        if (!work.isEmpty()) {
            int count = work.size();
            val results = new ArrayList<T>(count);
            for (int i = 0; i < count; i++)
                results.add(null);
            boolean hasRemainingFuture = false;
            do {
                if (hasRemainingFuture)
                    Thread.yield();
                hasRemainingFuture = false;
                for (int i = 0; i < count; i++) {
                    val future = work.get(i);
                    if (future == null)
                        continue;
                    if (!future.isDone()) {
                        hasRemainingFuture = true;
                        continue;
                    }
                    results.set(i, future.get());
                    work.set(i, null);
                }
            } while(hasRemainingFuture);
            return results;
        }
        return Collections.emptyList();
    }
    private static void awaitWork(List<Future<?>> work) throws ExecutionException, InterruptedException {
        if (!work.isEmpty()) {
            int count = work.size();
            boolean hasRemainingFuture = false;
            do {
                if (hasRemainingFuture)
                    Thread.yield();
                hasRemainingFuture = false;
                for (int i = 0; i < count; i++) {
                    val future = work.get(i);
                    if (future == null)
                        continue;
                    if (!future.isDone()) {
                        hasRemainingFuture = true;
                        continue;
                    }
                    future.get();
                    work.set(i, null);
                }
            } while(hasRemainingFuture);
        }
    }
    private static <T> void mergeLists(List<Collection<T>> lists, Collection<T> result) {
        for (val list: lists) {
            if (list != null)
                result.addAll(list);
        }
    }

    public static void segmentedRun(int size, SegmentedRunnable segmentedTask) throws ExecutionException, InterruptedException {
        awaitWork(segmentedDispatch(size, segmentedTask));
    }

    public static <T> List<T> segmentedRun(int size, SegmentedSupplier<T> segmentedTask) throws ExecutionException, InterruptedException {
        return resolveWork(segmentedDispatch(size, segmentedTask));
    }

    public static <T, E extends Collection<T>> E segmentedRunAndMerge(E accumulator, int size, SegmentedSupplier<Collection<T>> segmentedTask) throws ExecutionException, InterruptedException {
        mergeLists(resolveWork(segmentedDispatch(size, segmentedTask)), accumulator);
        return accumulator;
    }
}

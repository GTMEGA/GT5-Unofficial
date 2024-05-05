package gregtech.api.util.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

class UrgentAsyncTaskExecutor implements AutoCloseable{
    private final AtomicBoolean awake = new AtomicBoolean(false);
    private final AtomicBoolean alive = new AtomicBoolean(true);
    private final Semaphore S = new Semaphore(1);
    private final TinyTransferQueue<FutureTask<?>> tasks = new TinyTransferQueue<>(S);
    private final WorkerThread theThread = new WorkerThread();
    {
        theThread.start();
    }

    @Override
    public void close() {
        alive.set(false);
    }

    private class WorkerThread extends Thread {
        WorkerThread() {
            super("GT Parallel Worker");
        }

        @Override
        public void run() {
            while (alive.get()) {
                while (!S.tryAcquire())
                    Thread.yield();
                boolean acquired = true;
                try {
                    while (!awake.get()) {
                        S.release();
                        acquired = false;
                        if (!alive.get())
                            return;
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ignored) {
                        }
                        while (!S.tryAcquire())
                            Thread.yield();
                        acquired = true;
                    }
                    long lastWorkTime = System.currentTimeMillis();
                    while (!tasks.isEmpty() || System.currentTimeMillis() - lastWorkTime < 100) {
                        S.release();
                        acquired = false;
                        FutureTask<?> task1;
                        task1 = tasks.remove(100);
                        if (task1 == null) {
                            while (!S.tryAcquire())
                                Thread.yield();
                            acquired = true;
                            continue;
                        }

                        task1.run();
                        lastWorkTime = System.currentTimeMillis();
                        while (!S.tryAcquire())
                            Thread.yield();
                        acquired = true;
                    }
                    awake.set(false);
                } finally {
                    if (acquired)
                        S.release();
                }
            }
        }
    }

    private <T> Future<T> doSubmit(FutureTask<T> future) {
        while (!S.tryAcquire())
            Thread.yield();
        try {
            tasks.insert(future);
            if (!awake.getAndSet(true)) {
                theThread.interrupt();
            }
        } finally {
            S.release();
        }
        return future;
    }
    public Future<?> submit(Runnable task) {
        return doSubmit(new FutureTask<>(task, null));
    }
    public <T> Future<T> submit(Callable<T> task) {
        return doSubmit(new FutureTask<>(task));
    }
}

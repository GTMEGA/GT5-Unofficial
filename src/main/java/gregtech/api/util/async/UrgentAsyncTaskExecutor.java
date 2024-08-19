package gregtech.api.util.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

class UrgentAsyncTaskExecutor {
    private final AtomicBoolean awake = new AtomicBoolean(false);
    private final AtomicBoolean alive = new AtomicBoolean(false);
    private final Semaphore S = new Semaphore(1);
    private final TinyTransferQueue<FutureTask<?>> tasks = new TinyTransferQueue<>(S);
    private WorkerThread theThread;

    private class WorkerThread extends Thread {
        WorkerThread() {
            super("GT Parallel Worker");
        }

        @Override
        public void run() {
            while (!S.tryAcquire())
                Thread.yield();
            boolean acquired = true;
            long lastWorkTime = System.currentTimeMillis();
            try {
                alive:
                while (alive.get()) {
                    try {
                        while (!awake.get()) {
                            if (System.currentTimeMillis() - lastWorkTime > 10_000) {
                                alive.getAndSet(false);
                                theThread = null;
                                break alive;
                            }
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
                        lastWorkTime = System.currentTimeMillis();
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
                        if (!acquired)
                            while (!S.tryAcquire())
                                Thread.yield();
                        acquired = true;
                    }
                }
            } finally {
                if (acquired)
                    S.release();
                acquired = false;
            }
        }
    }

    private <T> Future<T> doSubmit(FutureTask<T> future) {
        while (!S.tryAcquire())
            Thread.yield();
        try {
            tasks.insert(future);
            if (!alive.getAndSet(true)) {
                theThread = new WorkerThread();
                theThread.start();
            }
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

package gregtech.api.util.async;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Semaphore;

@RequiredArgsConstructor
class TinyTransferQueue<T> {
    private static final int SIZE = 16;

    private final Semaphore S;
    private final Object[] STORE = new Object[SIZE];

    private volatile int insert = 0;
    private volatile int remove = 0;

    public void insert(T value) {
        int nextIndex = (insert + 1) % SIZE;
        while (nextIndex == remove) {
            S.release();
            Thread.yield();
            while (!S.tryAcquire())
                Thread.yield();
        }
        STORE[insert] = value;
        insert = nextIndex;
    }

    public T remove(int wait) {
        if (remove == insert) {
            long waitNanos = wait * 1_000_000L;
            long start = System.nanoTime();
            while (remove == insert) {
                Thread.yield();
                long now = System.nanoTime();
                if (now - start >= waitNanos)
                    return null;
            }
        }
        @SuppressWarnings("unchecked") T result = (T) STORE[remove];
        //noinspection NonAtomicOperationOnVolatileField
        remove = (remove + 1) % SIZE;
        return result;
    }

    public boolean isEmpty() {
        return remove == insert;
    }
}

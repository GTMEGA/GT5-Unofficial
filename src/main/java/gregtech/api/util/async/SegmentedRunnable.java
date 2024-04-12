package gregtech.api.util.async;

@FunctionalInterface
public interface SegmentedRunnable {
    void run(int start, int end);
}

package gregtech.api.util.async;

@FunctionalInterface
public interface SegmentedSupplier<T> {
    T get(int start, int end);
}

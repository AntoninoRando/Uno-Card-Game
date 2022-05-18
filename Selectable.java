public interface Selectable<T> {
    default public Selectable<T> getReference() {
        return this;
    }
}
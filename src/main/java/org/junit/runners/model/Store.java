package org.junit.runners.model;

public abstract class Store {
 
    /*
     * Key to use to retrieve values. Implementations must be immutable
     * and must implement equals() and hashCode().
     */
    public interface Key<T> {
    }

    /**
     * Gets the value that has been stored under the suppled {@code key}.
     *
     * @param key the key; never {@code null}
     * @return the value; potentially {@code null}
     */
    public abstract <T> T get(Key<T> key);

    /**
     * Stores a value for later retrieval user a suppled {@code key}.
     *
     * @param key the key under which the value will be stored; never {@code null}
     * @param value the value to store; may be {@code null}.
     * @return the previous value; may be potentially {@code null}
     */
    public abstract <T> T put(Key<T> key, T value);

    /**
     * Removes the value that was previously stored under a suppled {@code key}.
     *
     * @param key the key under which the value was stored; never {@code null}.
     * @return the previous value; may be potentially {@code null}
     */
    public abstract <T> T remove(Key<T> key);

    Store() {}
}

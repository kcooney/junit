package org.junit.runners.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HierarchicalStore extends Store {
    private final Store parentStore;
    private final ConcurrentMap<Object, StoredValue> values = new ConcurrentHashMap<Object, StoredValue>();

    public HierarchicalStore(Store parentStore) {
        this.parentStore = parentStore;
    }

    public HierarchicalStore() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Key<T> key) {
        StoredValue storedValue = values.get(checkNotNull(key, "key must not be null"));
        if (storedValue != null) {
            return (T) storedValue.value;
        }
        if (parentStore != null) {
            return parentStore.get(key);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T put(Key<T> key, T value) {
        StoredValue previousValue = values.put(key, new StoredValue(value));
        if (previousValue != null) {
            return (T) previousValue.value;
        }
        if (parentStore != null) {
            return parentStore.get(key);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T remove(Key<T> key) {
        StoredValue previousValue = values.remove(key);
        if (previousValue != null) {
            return (T) previousValue.value;
        }
        if (parentStore != null) {
            return parentStore.get(key);
        }
        return null;
    }

    private static <T> T checkNotNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

    private static class StoredValue {
        public final Object value;

        public StoredValue(Object value) {
            this.value = value;
        }
    }
}

package org.junit.runner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.runners.model.HierarchicalStore;
import org.junit.runners.model.Store;

public final class Stores {
    private final Map<Description, StoreData> storeDataMap;
 
    public static Stores create(Description topLevelDescripton) {
        Map<Description, StoreData> map = new HashMap<Description, StoreData>();
        StoreData storeData = new StoreData(new HierarchicalStore());
        addChildren(map, topLevelDescripton, storeData);
        return new Stores(Collections.unmodifiableMap(map));
    }

    Stores(Map<Description, StoreData> storeDataMap) {
        this.storeDataMap = storeDataMap;
    }

    private static void addChildren(Map<Description, StoreData> map,
            Description description, StoreData storeData) {
        map.put(description, storeData);
        for (Description child : description.getChildren()) {
            addChildren(map, child, new StoreData(description));
        }
    }

    public Store getStore(Description description) {
        StoreData data = storeDataMap.get(description);
        if (data == null) {
            throw new IllegalArgumentException("No Store for " + description);
        }
        return data.getOrCreateStore(this);
    }

    private static class StoreData {
        private final Lock lock = new ReentrantLock();
        private final Description parent;
        volatile Store store;

        StoreData(Description parent) {
            this.parent = parent;
        }

        StoreData(Store store) {
            this.parent = null;
            this.store = store;
        }

        Store getOrCreateStore(Stores stores) {
            Store currentStore = store; // volatile read
            if (currentStore != null) {
                return currentStore;
            }
            lock.lock();
            try {
                if (store == null) {
                    store = new HierarchicalStore(stores.getStore(parent));
                }
                return store;
            } finally {
                lock.unlock();
            }
        }
    }
}

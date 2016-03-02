package com.janosgyerik.dupfinder.dupfinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Track objects that are considered duplicates.
 * (Completely unrelated to logical equality by .equals, or identity by ==)
 *
 * @param <T>
 */
public class DuplicateTracker<T> {

    Map<T, Set<T>> pools = new HashMap<>();

    public void add(T item1, T item2) {
        Set<T> pool1 = pools.get(item1);
        Set<T> pool2 = pools.get(item2);

        if (pool1 == null && pool2 == null) {
            pool1 = new HashSet<>();
            pool1.add(item1);
            pool1.add(item2);
            pools.put(item1, pool1);
            pools.put(item2, pool1);
        } else if (pool1 == null) {
            pool2.add(item1);
            pools.put(item1, pool2);
        } else if (pool2 == null) {
            pool1.add(item2);
            pools.put(item2, pool1);
        } else if (pool1 != pool2) {
            pool1.addAll(pool2);
            for (T item : pool2) {
                pools.put(item, pool1);
            }
            pool2.clear();
        }
    }

    public Set<Set<T>> getDuplicates() {
        Set<Set<T>> duplicates = new HashSet<>();
        duplicates.addAll(pools.values());
        return duplicates;
    }
}

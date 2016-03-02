package com.janosgyerik.dupfinder.dupfinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DuplicateTracker<T> {

    Map<T, Set<T>> pools = new HashMap<>();

    public void add(T file1, T file2) {
        Set<T> pool1 = pools.get(file1);
        Set<T> pool2 = pools.get(file2);

        if (pool1 == null && pool2 == null) {
            pool1 = new HashSet<>();
            pool1.add(file1);
            pool1.add(file2);
            pools.put(file1, pool1);
            pools.put(file2, pool1);
        } else if (pool1 == null) {
            pool2.add(file1);
            pools.put(file1, pool2);
        } else if (pool2 == null) {
            pool1.add(file2);
            pools.put(file2, pool1);
        } else if (pool1 != pool2) {
            pool1.addAll(pool2);
            for (T file : pool2) {
                pools.put(file, pool1);
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

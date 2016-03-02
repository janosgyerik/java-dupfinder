package com.janosgyerik.dupfinder.dupfinder;

import java.io.*;
import java.util.*;

public class DuplicateFileFinderImpl implements DuplicateFileFinder {

    private static class DuplicateTracker {
        Map<File, Set<File>> pools = new HashMap<>();

        public void add(File file1, File file2) {
            Set<File> pool1 = pools.get(file1);
            Set<File> pool2 = pools.get(file2);

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
                for (File file : pool2) {
                    pools.put(file, pool1);
                }
            }
        }

        public Set<Set<File>> getDuplicates() {
            Set<Set<File>> duplicates = new HashSet<>();
            duplicates.addAll(pools.values());
            return duplicates;
        }
    }

    @Override
    public Set<Set<File>> findDuplicates(List<File> files) {
        DuplicateTracker tracker = new DuplicateTracker();
        FileContentComparator comparator = new FileContentComparator();

        Collections.sort(files, (file1, file2) -> {
            int cmp = comparator.compare(file1, file2);
            if (cmp == 0) {
                tracker.add(file1, file2);
            }
            return cmp;
        });
        return tracker.getDuplicates();
    }
}

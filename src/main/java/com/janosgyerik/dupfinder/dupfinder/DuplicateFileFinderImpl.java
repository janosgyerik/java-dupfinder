package com.janosgyerik.dupfinder.dupfinder;

import java.io.*;
import java.util.*;

public class DuplicateFileFinderImpl implements DuplicateFileFinder {

    @Override
    public Set<Set<File>> findDuplicates(List<File> files) {
        DuplicateTracker<File> tracker = new DuplicateTracker<>();
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

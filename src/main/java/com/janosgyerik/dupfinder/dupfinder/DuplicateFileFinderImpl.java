package com.janosgyerik.dupfinder.dupfinder;

import java.io.*;
import java.util.*;

public class DuplicateFileFinderImpl implements DuplicateFileFinder {

    private static class ContentComparator implements Comparator<File> {

        private static final int BUFSIZE = 4096;

        private final DuplicateTracker tracker;

        public ContentComparator(DuplicateTracker tracker) {
            this.tracker = tracker;
        }

        @Override
        public int compare(File file1, File file2) {
            int compareFileSize = Long.compare(file1.length(), file2.length());
            if (compareFileSize != 0) {
                return compareFileSize;
            }

            if (file1.length() == 0) {
                tracker.add(file1, file2);
                return 0;
            }

            byte[] buffer1 = new byte[BUFSIZE];
            byte[] buffer2 = new byte[BUFSIZE];

            try (BufferedInputStream stream1 = new BufferedInputStream(new FileInputStream(file1));
                 BufferedInputStream stream2 = new BufferedInputStream(new FileInputStream(file2))) {
                int bytesRead1;
                int bytesRead2;

                while (true) {
                    bytesRead1 = stream1.read(buffer1);
                    bytesRead2 = stream2.read(buffer2);

                    assert bytesRead1 == bytesRead2;

                    if (bytesRead1 == -1) {
                        break;
                    }

                    int compareBuffers = compareBuffers(buffer1, buffer2, bytesRead1);
                    if (compareBuffers != 0) {
                        return compareBuffers;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return file1.compareTo(file2);
            }

            tracker.add(file1, file2);
            return 0;
        }

        private int compareBuffers(byte[] buffer1, byte[] buffer2, int num) {
            for (int i = 0; i < num; ++i) {
                int compare = Byte.compare(buffer1[i], buffer2[i]);
                if (compare != 0) {
                    return compare;
                }
            }
            return 0;
        }
    }

    private static class DuplicateTracker {
        Map<File, Set<File>> pools = new HashMap<>();

        public void add(File file1, File file2) {
            Set<File> pool1 = pools.get(file1);
            Set<File> pool2 = pools.get(file2);

            final Set<File> pool;

            if (pool1 == null && pool2 == null) {
                pool = new HashSet<>();
                pool.add(file1);
                pool.add(file2);
            } else if (pool1 == null) {
                pool = pool2;
                pool.add(file1);
            } else if (pool2 == null) {
                pool = pool1;
                pool.add(file2);
            } else if (pool1 == pool2) {
                pool = pool1;
            } else {
                pool = pool1;
                pool.addAll(pool2);
                for (File file : pool2) {
                    pools.put(file, pool);
                }
            }
            pools.put(file1, pool);
            pools.put(file2, pool);
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
        Collections.sort(files, new ContentComparator(tracker));
        return tracker.getDuplicates();
    }
}

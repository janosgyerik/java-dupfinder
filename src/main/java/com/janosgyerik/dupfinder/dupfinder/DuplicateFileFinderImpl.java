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
            int cmpFileSize = Long.compare(file1.length(), file2.length());
            if (cmpFileSize != 0) {
                return cmpFileSize;
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

                    int cmpBuffers = compareBuffers(buffer1, buffer2, bytesRead1);
                    if (cmpBuffers != 0) {
                        return cmpBuffers;
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
                int cmp = Byte.compare(buffer1[i], buffer2[i]);
                if (cmp != 0) {
                    return cmp;
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
        Collections.sort(files, new ContentComparator(tracker));
        return tracker.getDuplicates();
    }
}

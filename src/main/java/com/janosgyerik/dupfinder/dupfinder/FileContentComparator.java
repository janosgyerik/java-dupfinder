package com.janosgyerik.dupfinder.dupfinder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;

/**
 * A comparator to compare two files by content.
 * In case of I/O errors, fall back to default comparator of File.
 */
public class FileContentComparator implements Comparator<File> {

    private static final int DEFAULT_BUFSIZE = 1024 * 1024;
    private final int bufSize;

    public FileContentComparator() {
        bufSize = DEFAULT_BUFSIZE;
    }

    public FileContentComparator(int bufSize) {
        this.bufSize = bufSize;
    }

    @Override
    public int compare(File file1, File file2) {
        int cmpFileSize = Long.compare(file1.length(), file2.length());
        if (cmpFileSize != 0) {
            return cmpFileSize;
        }

        if (file1.length() == 0) {
            return 0;
        }

        byte[] buffer1 = new byte[bufSize];
        byte[] buffer2 = new byte[bufSize];

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
            // ignore I/O errors, fall back to default comparison logic
            return file1.compareTo(file2);
        }

        return 0;
    }

    private int compareBuffers(byte[] buffer1, byte[] buffer2, int size) {
        for (int i = 0; i < size; ++i) {
            int cmp = Byte.compare(buffer1[i], buffer2[i]);
            if (cmp != 0) {
                return cmp;
            }
        }
        return 0;
    }
}

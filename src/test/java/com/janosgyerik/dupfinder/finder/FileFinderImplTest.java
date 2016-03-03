package com.janosgyerik.dupfinder.finder;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static com.janosgyerik.dupfinder.FileFilters.any;
import static com.janosgyerik.dupfinder.FileFilters.byExtension;
import static com.janosgyerik.dupfinder.FileFilters.byMaxDepth;
import static com.janosgyerik.dupfinder.utils.TestFileUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileFinderImplTest {

    private final FileFinder fileFinder = new FileFinderImpl();

    @Test
    public void test_find_nothing_in_empty_dir() throws IOException {
        File emptyDir = createTempDir();
        assertTrue(emptyDir.isDirectory());
        assertEquals(0, fileFinder.find(emptyDir, any()).size());
    }

    @Test
    public void test_find_one_file_in_basedir() throws IOException {
        File tempDir = createTempDir();
        File tempFile = createTempFile(tempDir);
        assertEquals(Collections.singletonList(tempFile), fileFinder.find(tempDir, any()));
    }

    @Test
    public void test_find_two_files_in_basedir() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);
        File tempFile2 = createTempFile(tempDir);
        assertEquals(sorted(tempFile1, tempFile2), fileFinder.find(tempDir, any()));
    }

    @Test
    public void test_find_two_files_in_tree() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);

        File subDir = createTempDir(tempDir);
        File tempFile2 = createTempFile(subDir);

        assertEquals(sorted(tempFile1, tempFile2), fileFinder.find(tempDir, any()));
    }

    @Test
    public void test_find_one_file_until_depth() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);

        File subDir = createTempDir(tempDir);
        File tempFile2 = createTempFile(subDir);

        assertEquals(sorted(tempFile1, tempFile2), fileFinder.find(tempDir, any()));
        assertEquals(sorted(tempFile1), fileFinder.find(tempDir, byMaxDepth(tempDir, 1)));
    }

    @Test
    public void test_find_one_file_matching_extension() throws IOException {
        File tempDir = createTempDir();

        final String suffix = ".tmp";
        File tempFile1 = createTempFile(tempDir, suffix);
        File tempFile2 = createTempFile(tempDir, ".exe");
        assertEquals(sorted(tempFile1, tempFile2), fileFinder.find(tempDir, any()));

        assertEquals(sorted(tempFile1), fileFinder.find(tempDir, byExtension(suffix)));
    }

    @Test
    public void test_find_one_file_until_accessible() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);

        File subDir = createTempDir(tempDir);
        File tempFile2 = createTempFile(subDir);

        assertEquals(sorted(tempFile1, tempFile2), fileFinder.find(tempDir, any()));

        if (!subDir.setExecutable(false)) {
            throw new IOException("could not revoke directory executable permission");
        }
        assertEquals(sorted(tempFile1), fileFinder.find(tempDir, byMaxDepth(tempDir, 1)));

        if (!subDir.setExecutable(true)) {
            throw new IOException("could not restore directory executable permission");
        }
        assertEquals(sorted(tempFile1, tempFile2), fileFinder.find(tempDir, any()));
    }
}

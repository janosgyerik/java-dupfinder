package com.janosgyerik.duplicatefilefinder;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileFinderImplTest {

    private File createTempDir() throws IOException {
        File tempFile = File.createTempFile("tmp", "-dir");
        if (tempFile.delete() && tempFile.mkdir()) {
            return tempFile;
        }
        throw new IOException("could not create temporary directory");
    }

    private File createSubDir(File basedir, String name) throws IOException {
        File dir = new File(basedir, name);
        if (!dir.mkdir()) {
            throw new IOException("could not create directory: " + dir);
        }
        return dir;
    }

    private File createTempFile(File basedir) throws IOException {
        return File.createTempFile("tmp", ".tmp", basedir);
    }

    private List<File> sorted(File... files) {
        List<File> list = new ArrayList<>(Arrays.asList(files));
        Collections.sort(list);
        return list;
    }

    @Test
    public void test_find_nothing_in_empty_dir() throws IOException {
        File emptyDir = createTempDir();
        assertTrue(emptyDir.isDirectory());
        assertEquals(0, new FileFinderImpl().find(emptyDir).size());
    }

    @Test
    public void test_find_one_file_in_basedir() throws IOException {
        File tempDir = createTempDir();
        File tempFile = createTempFile(tempDir);
        assertEquals(Collections.singletonList(tempFile), new FileFinderImpl().find(tempDir));
    }

    @Test
    public void test_find_two_files_in_basedir() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);
        File tempFile2 = createTempFile(tempDir);
        assertEquals(sorted(tempFile1, tempFile2), new FileFinderImpl().find(tempDir));
    }

    @Test
    public void test_find_two_files_in_tree() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);

        File subDir = createSubDir(tempDir, "sub1");
        File tempFile2 = createTempFile(subDir);

        assertEquals(sorted(tempFile1, tempFile2), new FileFinderImpl().find(tempDir));
    }

    @Test
    public void test_find_one_file_until_depth() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);

        File subDir = createSubDir(tempDir, "sub1");
        File tempFile2 = createTempFile(subDir);

        assertEquals(sorted(tempFile1, tempFile2), new FileFinderImpl().find(tempDir));
        assertEquals(sorted(tempFile1), new FileFinderImpl().find(tempDir, 1));
    }
}

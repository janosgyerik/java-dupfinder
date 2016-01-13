package com.janosgyerik.duplicatefilefinder.finder;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileFinderImplTest {

    private static final FileFinder FINDER = new FileFinderImpl();

    private File createTempDir() throws IOException {
        File dir = Files.createTempDirectory("tmp").toFile();
        dir.deleteOnExit();
        return dir;
    }

    private File createTempDir(File basedir) throws IOException {
        File dir = Files.createTempDirectory(basedir.toPath(), "tmp").toFile();
        dir.deleteOnExit();
        return dir;
    }

    private File createTempFile(File basedir) throws IOException {
        return createTempFile(basedir, ".tmp");
    }

    private File createTempFile(File basedir, String suffix) throws IOException {
        File file = File.createTempFile("tmp", suffix, basedir);
        file.deleteOnExit();
        return file;
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
        assertEquals(0, FINDER.find(emptyDir).size());
    }

    @Test
    public void test_find_one_file_in_basedir() throws IOException {
        File tempDir = createTempDir();
        File tempFile = createTempFile(tempDir);
        assertEquals(Collections.singletonList(tempFile), FINDER.find(tempDir));
    }

    @Test
    public void test_find_two_files_in_basedir() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);
        File tempFile2 = createTempFile(tempDir);
        assertEquals(sorted(tempFile1, tempFile2), FINDER.find(tempDir));
    }

    @Test
    public void test_find_two_files_in_tree() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);

        File subDir = createTempDir(tempDir);
        File tempFile2 = createTempFile(subDir);

        assertEquals(sorted(tempFile1, tempFile2), FINDER.find(tempDir));
    }

    @Test
    public void test_find_one_file_until_depth() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);

        File subDir = createTempDir(tempDir);
        File tempFile2 = createTempFile(subDir);

        assertEquals(sorted(tempFile1, tempFile2), FINDER.find(tempDir));
        assertEquals(sorted(tempFile1), FINDER.find(tempDir, 1));
    }

    @Test
    public void test_find_one_file_matching_extension() throws IOException {
        File tempDir = createTempDir();

        final String suffix = ".tmp";
        File tempFile1 = createTempFile(tempDir, suffix);
        File tempFile2 = createTempFile(tempDir, ".exe");
        assertEquals(sorted(tempFile1, tempFile2), FINDER.find(tempDir));

        assertEquals(sorted(tempFile1), FINDER.find(tempDir, pathname -> {
            return pathname.getName().endsWith(suffix);
        }));
    }

    @Test
    public void test_find_one_file_until_accessible() throws IOException {
        File tempDir = createTempDir();
        File tempFile1 = createTempFile(tempDir);

        File subDir = createTempDir(tempDir);
        File tempFile2 = createTempFile(subDir);

        assertEquals(sorted(tempFile1, tempFile2), FINDER.find(tempDir));

        if (!subDir.setExecutable(false)) {
            throw new IOException("could not revoke directory executable permission");
        }
        assertEquals(sorted(tempFile1), FINDER.find(tempDir, 1));

        if (!subDir.setExecutable(true)) {
            throw new IOException("could not restore directory executable permission");
        }
        assertEquals(sorted(tempFile1, tempFile2), FINDER.find(tempDir));
    }
}

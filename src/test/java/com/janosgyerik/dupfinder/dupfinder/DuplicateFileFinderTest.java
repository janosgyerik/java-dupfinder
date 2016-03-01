package com.janosgyerik.dupfinder.dupfinder;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.janosgyerik.dupfinder.utils.TestFileUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DuplicateFileFinderTest {

    private static final DuplicateFileFinder DUPFINDER = new DuplicateFileFinderImpl();

    private File createTempFileWithContent(File basedir, String content) throws IOException {
        File file = createTempFile(basedir);
        FileWriter writer = new FileWriter(file);
        writer.append(content);
        writer.close();
        return file;
    }

    private Set<File> toSet(File... files) {
        Set<File> set = new HashSet<>();
        set.addAll(Arrays.asList(files));
        return set;
    }

    private List<File> listFiles(File basedir) {
        File[] files = basedir.listFiles();
        assertNotNull(files);
        return Arrays.asList(files);
    }

    @Test
    public void test_find_no_duplicates() throws IOException {
        File basedir = createTempDir();
        File file1 = createTempFileWithContent(basedir, "foo");
        File file2 = createTempFileWithContent(basedir, "bar");

        List<File> files = listFiles(basedir);

        assertEquals(sorted(file1, file2), files);

        assertEquals(0, DUPFINDER.findDuplicates(files).size());
    }

    @Test
    public void test_find_2_of_2_duplicates() throws IOException {
        String content = "blah";

        File basedir = createTempDir();
        File file1 = createTempFileWithContent(basedir, content);
        File file2 = createTempFileWithContent(basedir, content);

        List<File> files = listFiles(basedir);

        assertEquals(sorted(file1, file2), files);

        Set<Set<File>> duplicates = Collections.singleton(toSet(file1, file2));
        assertEquals(duplicates, DUPFINDER.findDuplicates(files));
    }

    @Test
    public void test_find_5_of_5_duplicates() throws IOException {
        String content = "blah";

        File basedir = createTempDir();
        File file1 = createTempFileWithContent(basedir, content);
        File file2 = createTempFileWithContent(basedir, content);
        File file3 = createTempFileWithContent(basedir, content);
        File file4 = createTempFileWithContent(basedir, content);
        File file5 = createTempFileWithContent(basedir, content);

        List<File> files = listFiles(basedir);

        assertEquals(sorted(file1, file2, file3, file4, file5), files);

        Set<Set<File>> duplicates = Collections.singleton(toSet(file1, file2, file3, file4, file5));
        assertEquals(duplicates, DUPFINDER.findDuplicates(files));
    }

    @Test
    public void test_find_2_of_3_duplicates() throws IOException {
        String content = "blah";
        String differentContent = "balm";

        File basedir = createTempDir();
        File file1 = createTempFileWithContent(basedir, content);
        File file2 = createTempFileWithContent(basedir, content);
        File file3 = createTempFileWithContent(basedir, differentContent);

        List<File> files = listFiles(basedir);

        assertEquals(sorted(file1, file2, file3), files);

        Set<Set<File>> duplicates = Collections.singleton(toSet(file1, file2));
        assertEquals(duplicates, DUPFINDER.findDuplicates(files));
    }

    @Test
    public void test_find_2_sets_of_duplicates_among_5() throws IOException {
        String content = "blah";
        String anotherContent = "balm";
        String yetAnotherContent = "bulk";

        File basedir = createTempDir();
        File file1 = createTempFileWithContent(basedir, content);
        File file2 = createTempFileWithContent(basedir, content);
        File file3 = createTempFileWithContent(basedir, anotherContent);
        File file4 = createTempFileWithContent(basedir, anotherContent);
        File file5 = createTempFileWithContent(basedir, yetAnotherContent);

        List<File> files = listFiles(basedir);

        assertEquals(sorted(file1, file2, file3, file4, file5), files);

        Set<Set<File>> duplicates = new HashSet<>(Arrays.asList(
                toSet(file1, file2),
                toSet(file3, file4)
        ));
        assertEquals(duplicates, DUPFINDER.findDuplicates(files));
    }
}

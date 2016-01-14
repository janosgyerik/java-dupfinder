package com.janosgyerik.dupfinder.dupfinder;

import com.janosgyerik.dupfinder.finder.FileFinder;
import com.janosgyerik.dupfinder.finder.FileFinderImpl;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.janosgyerik.dupfinder.utils.TestFileUtils.*;
import static org.junit.Assert.assertEquals;

public class DuplicateFileFinderTest {

    private static final FileFinder FINDER = new FileFinderImpl();
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

    @Test
    public void test_find_no_duplicates() throws IOException {
        File basedir = createTempDir();
        File file1 = createTempFileWithContent(basedir, "foo");
        File file2 = createTempFileWithContent(basedir, "bar");

        List<File> files = FINDER.find(basedir);

        assertEquals(sorted(file1, file2), files);

        assertEquals(0, DUPFINDER.findDuplicates(files).size());
    }

    @Test
    public void test_find_all_are_duplicates() throws IOException {
        String content = "blah";

        File basedir = createTempDir();
        File file1 = createTempFileWithContent(basedir, content);
        File file2 = createTempFileWithContent(basedir, content);

        List<File> files = FINDER.find(basedir);

        assertEquals(sorted(file1, file2), files);

        Set<Set<File>> duplicates = Collections.singleton(toSet(file1, file2));
        assertEquals(duplicates, DUPFINDER.findDuplicates(files));
    }

    @Test
    public void test_find_some_are_duplicates() throws IOException {
        String content = "blah";
        String differentContent = "balm";

        File basedir = createTempDir();
        File file1 = createTempFileWithContent(basedir, content);
        File file2 = createTempFileWithContent(basedir, content);
        File file3 = createTempFileWithContent(basedir, differentContent);

        List<File> files = FINDER.find(basedir);

        assertEquals(sorted(file1, file2, file3), files);

        Set<Set<File>> duplicates = Collections.singleton(toSet(file1, file2));
        assertEquals(duplicates, DUPFINDER.findDuplicates(files));
    }
}

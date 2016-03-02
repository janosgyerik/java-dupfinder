package com.janosgyerik.dupfinder.dupfinder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class DuplicateFileFinderTest {

    private final DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinder();

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private File createTempFileWithContent(String content) throws IOException {
        File file = tmpDir.newFile();
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
    public void should_find_no_duplicates() throws IOException {
        File file1 = createTempFileWithContent("foo");
        File file2 = createTempFileWithContent("bar");

        List<File> files = Arrays.asList(file1, file2);

        assertEquals(0, duplicateFileFinder.findDuplicates(files).size());
    }

    @Test
    public void should_find_2_of_2_duplicates() throws IOException {
        String content = "blah";

        File file1 = createTempFileWithContent(content);
        File file2 = createTempFileWithContent(content);

        List<File> files = Arrays.asList(file1, file2);

        Set<Set<File>> duplicates = Collections.singleton(toSet(file1, file2));
        assertEquals(duplicates, duplicateFileFinder.findDuplicates(files));
    }

    @Test
    public void should_find_5_of_5_duplicates() throws IOException {
        String content = "blah";

        File file1 = createTempFileWithContent(content);
        File file2 = createTempFileWithContent(content);
        File file3 = createTempFileWithContent(content);
        File file4 = createTempFileWithContent(content);
        File file5 = createTempFileWithContent(content);

        List<File> files = Arrays.asList(file1, file2, file3, file4, file5);

        Set<Set<File>> duplicates = Collections.singleton(toSet(file1, file2, file3, file4, file5));
        assertEquals(duplicates, duplicateFileFinder.findDuplicates(files));
    }

    @Test
    public void should_find_2_of_3_duplicates() throws IOException {
        String content = "blah";
        String differentContent = "balm";

        File file1 = createTempFileWithContent(content);
        File file2 = createTempFileWithContent(content);
        File file3 = createTempFileWithContent(differentContent);

        List<File> files = Arrays.asList(file1, file2, file3);

        Set<Set<File>> duplicates = Collections.singleton(toSet(file1, file2));
        assertEquals(duplicates, duplicateFileFinder.findDuplicates(files));
    }

    @Test
    public void should_find_2_sets_of_duplicates_among_5() throws IOException {
        String content = "blah";
        String anotherContent = "balm";
        String yetAnotherContent = "bulk";

        File file1 = createTempFileWithContent(content);
        File file2 = createTempFileWithContent(content);
        File file3 = createTempFileWithContent(anotherContent);
        File file4 = createTempFileWithContent(anotherContent);
        File file5 = createTempFileWithContent(yetAnotherContent);

        List<File> files = Arrays.asList(file1, file2, file3, file4, file5);

        Set<Set<File>> duplicates = new HashSet<>(Arrays.asList(
                toSet(file1, file2),
                toSet(file3, file4)
        ));
        assertEquals(duplicates, duplicateFileFinder.findDuplicates(files));
    }
}

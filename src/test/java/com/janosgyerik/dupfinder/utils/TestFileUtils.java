package com.janosgyerik.dupfinder.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TestFileUtils {

    private TestFileUtils() {
        throw new AssertionError("utility class, forbidden constructor");
    }

    public static File createTempFile(File basedir) throws IOException {
        return createTempFile(basedir, ".tmp");
    }

    public static File createTempFile(File basedir, String suffix) throws IOException {
        File file = File.createTempFile("tmp", suffix, basedir);
        file.deleteOnExit();
        return file;
    }

    public static List<File> sorted(File... files) {
        List<File> list = new ArrayList<>(Arrays.asList(files));
        Collections.sort(list);
        return list;
    }
}

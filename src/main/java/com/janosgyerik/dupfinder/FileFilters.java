package com.janosgyerik.dupfinder;

import java.io.File;
import java.io.FileFilter;

public class FileFilters {
    public static FileFilter any() {
        return pathname -> true;
    }

    public static FileFilter byExtension(String extension) {
        return pathname -> pathname.isDirectory() || pathname.getName().endsWith(extension);
    }

    public static FileFilter byMaxDepth(File basedir, int maxDepth) {
        String prefix = basedir.toString();
        return pathname -> pathname.toString().replace(prefix, "").split("/").length - 1 <= maxDepth;
    }
}

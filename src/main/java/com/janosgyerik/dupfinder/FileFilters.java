package com.janosgyerik.dupfinder;

import java.io.FileFilter;

public class FileFilters {
    public static FileFilter any() {
        return pathname -> true;
    }

    public static FileFilter byExtension(String extension) {
        return pathname -> pathname.isDirectory() || pathname.getName().endsWith(extension);
    }
}

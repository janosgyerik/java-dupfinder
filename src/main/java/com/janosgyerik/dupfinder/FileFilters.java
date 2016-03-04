package com.janosgyerik.dupfinder;

import java.io.File;
import java.io.FileFilter;

public class FileFilters {
    public static FileFilter any() {
        return file -> true;
    }

    public static FileFilter nonEmpty() {
        return file -> file.length() > 0;
    }

    public static FileFilter byExtension(String extension) {
        return file -> file.isDirectory() || file.getName().endsWith(extension);
    }

    public static FileFilter byMaxDepth(File basedir, int maxDepth) {
        String prefix = basedir.toString();
        return file -> file.toString().replace(prefix, "").split("/").length - 1 <= maxDepth;
    }

    public static FileFilter composite(FileFilter... fileFilters) {
        return file -> {
            for (FileFilter fileFilter : fileFilters) {
                if (!fileFilter.accept(file)) {
                    return false;
                }
            }
            return true;
        };
    }
}

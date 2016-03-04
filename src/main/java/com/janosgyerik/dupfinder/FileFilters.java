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
        return file -> countSeparators(file.toString().replace(prefix, "")) <= maxDepth;
    }

    protected static int countSeparators(String path) {
        int count = 0;
        int index = 1;
        do {
            index = path.indexOf(File.separator, index + 1);
            ++count;
        } while (index > 0);
        return count;
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

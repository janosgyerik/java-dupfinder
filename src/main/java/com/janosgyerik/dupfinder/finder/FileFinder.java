package com.janosgyerik.dupfinder.finder;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public interface FileFinder {

    /**
     * Find files, by searching recursively through a directory tree.
     *
     * @param basedir The base directory to start searching
     * @param fileFilter A filter to decide which files to include
     * @return list of matching files found
     */
    List<File> find(File basedir, FileFilter fileFilter);
}

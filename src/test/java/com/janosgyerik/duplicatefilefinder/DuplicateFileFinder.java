package com.janosgyerik.duplicatefilefinder;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public interface DuplicateFileFinder {

    /**
     * Find duplicate files, by searching recursively through a directory tree.
     *
     * @param basedir The base directory to start searching
     * @param fileFilter A filter to decide which files to include
     * @return sets of duplicate files
     */
    Set<Set<File>> findDuplicateFiles(File basedir, FileFilter fileFilter, int depth);

    Set<Set<File>> findDuplicateFiles(File basedir, FileFilter fileFilter);

    Set<Set<File>> findDuplicateFiles(File basedir, int depth);

    Set<Set<File>> findDuplicateFiles(File basedir);

}

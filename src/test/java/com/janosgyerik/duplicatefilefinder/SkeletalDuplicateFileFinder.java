package com.janosgyerik.duplicatefilefinder;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public abstract class SkeletalDuplicateFileFinder implements DuplicateFileFinder {

    private static final int MAX_DEPTH = Integer.MAX_VALUE;

    private static final FileFilter ACCEPT_ALL_FILES = pathname -> true;

    public abstract Set<Set<File>> findDuplicateFiles(File basedir, FileFilter fileFilter, int depth);

    @Override
    public Set<Set<File>> findDuplicateFiles(File basedir, FileFilter fileFilter) {
        return findDuplicateFiles(basedir, fileFilter, MAX_DEPTH);
    }

    @Override
    public Set<Set<File>> findDuplicateFiles(File basedir, int depth) {
        return findDuplicateFiles(basedir, ACCEPT_ALL_FILES, depth);
    }

    @Override
    public Set<Set<File>> findDuplicateFiles(File basedir) {
        return findDuplicateFiles(basedir, ACCEPT_ALL_FILES, MAX_DEPTH);
    }
}

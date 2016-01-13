package com.janosgyerik.duplicatefilefinder;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public abstract class SkeletalFileFinder implements FileFinder {

    private static final int MAX_DEPTH = Integer.MAX_VALUE;

    private static final FileFilter ACCEPT_ALL_FILES = pathname -> true;

    public abstract List<File> find(File basedir, FileFilter fileFilter, int depth);

    @Override
    public List<File> find(File basedir, FileFilter fileFilter) {
        return find(basedir, fileFilter, MAX_DEPTH);
    }

    @Override
    public List<File> find(File basedir, int depth) {
        return find(basedir, ACCEPT_ALL_FILES, depth);
    }

    @Override
    public List<File> find(File basedir) {
        return find(basedir, ACCEPT_ALL_FILES, MAX_DEPTH);
    }
}

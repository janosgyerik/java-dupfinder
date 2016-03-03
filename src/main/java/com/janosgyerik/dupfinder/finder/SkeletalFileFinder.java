package com.janosgyerik.dupfinder.finder;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import static com.janosgyerik.dupfinder.FileFilters.any;

abstract class SkeletalFileFinder implements FileFinder {

    private static final int MAX_DEPTH = Integer.MAX_VALUE;

    public abstract List<File> find(File basedir, FileFilter fileFilter, int depth);

    @Override
    public List<File> find(File basedir, FileFilter fileFilter) {
        return find(basedir, fileFilter, MAX_DEPTH);
    }

    @Override
    public List<File> find(File basedir, int depth) {
        return find(basedir, any(), depth);
    }

    @Override
    public List<File> find(File basedir) {
        return find(basedir, any(), MAX_DEPTH);
    }
}

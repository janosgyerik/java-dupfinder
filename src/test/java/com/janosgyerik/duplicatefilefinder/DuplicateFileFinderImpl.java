package com.janosgyerik.duplicatefilefinder;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public class DuplicateFileFinderImpl extends SkeletalDuplicateFileFinder {
    @Override
    public Set<Set<File>> findDuplicateFiles(File basedir, FileFilter fileFilter, int depth) {
        return null;
    }
}

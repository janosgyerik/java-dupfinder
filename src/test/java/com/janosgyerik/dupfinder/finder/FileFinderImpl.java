package com.janosgyerik.dupfinder.finder;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FileFinderImpl extends SkeletalFileFinder {
    @Override
    public List<File> find(File basedir, FileFilter fileFilter, int depth) {
        List<File> files = new ArrayList<>();
        find(basedir, fileFilter, depth, files);
        return files;
    }

    public void find(File basedir, FileFilter fileFilter, int depth, List<File> acc) {
        if (depth <= 0) {
            return;
        }
        File[] files = basedir.listFiles(fileFilter);
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    acc.add(file);
                } else if (file.isDirectory()) {
                    find(file, fileFilter, depth - 1, acc);
                }
            }
        }
    }
}

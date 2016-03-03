package com.janosgyerik.dupfinder.finder;

import com.janosgyerik.dupfinder.FileFilters;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import static com.janosgyerik.dupfinder.FileFilters.byMaxDepth;

public class FileFinderImpl extends SkeletalFileFinder {
    @Override
    public List<File> find(File basedir, FileFilter fileFilter, int depth) {
        List<File> files = new ArrayList<>();
        FileFilter filter = FileFilters.composite(fileFilter, byMaxDepth(basedir, depth));
        find(basedir, filter, files);
        return files;
    }

    private void find(File basedir, FileFilter fileFilter, List<File> acc) {
        File[] files = basedir.listFiles(fileFilter);
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    acc.add(file);
                } else if (file.isDirectory()) {
                    find(file, fileFilter, acc);
                }
            }
        }
    }
}

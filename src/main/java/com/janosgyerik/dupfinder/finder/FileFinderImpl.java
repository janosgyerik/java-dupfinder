package com.janosgyerik.dupfinder.finder;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import static com.janosgyerik.dupfinder.FileFilters.byMaxDepth;

public class FileFinderImpl extends SkeletalFileFinder {
    private static class CompositeFileFilter implements FileFilter {
        List<FileFilter> fileFilters = new ArrayList<>();

        @Override
        public boolean accept(File pathname) {
            for (FileFilter fileFilter : fileFilters) {
                if (!fileFilter.accept(pathname)) {
                    return false;
                }
            }
            return true;
        }

        public void add(FileFilter fileFilter) {
            fileFilters.add(fileFilter);
        }
    }

    @Override
    public List<File> find(File basedir, FileFilter fileFilter, int depth) {
        List<File> files = new ArrayList<>();
        CompositeFileFilter compositeFileFilter = new CompositeFileFilter();
        compositeFileFilter.add(fileFilter);
        compositeFileFilter.add(byMaxDepth(basedir, depth));
        find(basedir, compositeFileFilter, files);
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

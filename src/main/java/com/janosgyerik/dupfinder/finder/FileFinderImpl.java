package com.janosgyerik.dupfinder.finder;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FileFinderImpl implements FileFinder {

    @Override
    public List<File> find(File basedir, FileFilter fileFilter) {
        List<File> files = new ArrayList<>();
        find(basedir, fileFilter, files);
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

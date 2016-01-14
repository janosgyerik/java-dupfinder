package com.janosgyerik.dupfinder.dupfinder;

import java.io.File;
import java.util.List;

public interface DuplicateFileFinder {

    /**
     * Find duplicate files in given list
     *
     * @param files the list of files to check
     * @return sets of duplicate files
     */
    List<List<File>> findDuplicates(List<File> files);

}

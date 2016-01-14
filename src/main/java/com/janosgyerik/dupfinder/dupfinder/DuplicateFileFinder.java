package com.janosgyerik.dupfinder.dupfinder;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface DuplicateFileFinder {

    /**
     * Find duplicate files in given list
     *
     * @param files the list of files to check
     * @return sets of duplicate files
     */
    Set<Set<File>> findDuplicates(List<File> files);

}

package com.janosgyerik.duplicatefilefinder.dupfinder;

import com.janosgyerik.duplicatefilefinder.finder.FileFinder;

import java.io.File;
import java.util.List;

public interface DuplicateFileFinder {

    /**
     * Find duplicate files, by searching recursively through a directory tree.
     *
     * @param fileFinder the file finder to provide the list of files to check
     * @return sets of duplicate files
     */
    List<List<File>> findDuplicateFiles(FileFinder fileFinder);

}

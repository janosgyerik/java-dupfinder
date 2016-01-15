package com.janosgyerik.dupfinder;

import com.janosgyerik.dupfinder.dupfinder.DuplicateFileFinder;
import com.janosgyerik.dupfinder.dupfinder.DuplicateFileFinderImpl;
import com.janosgyerik.dupfinder.finder.FileFinder;
import com.janosgyerik.dupfinder.finder.FileFinderImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
        }

        FileFinder finder = new FileFinderImpl();
        DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinderImpl();

        for (String arg : args) {
            File dir = new File(arg);
            if (isAccessibleDirectory(dir)) {
                List<File> files = finder.find(dir);
                Set<Set<File>> duplicateFileSets = duplicateFileFinder.findDuplicates(files);
                for (Set<File> fileSet : duplicateFileSets) {
                    printFileSet(fileSet);
                }
            }
        }
    }

    private static boolean isAccessibleDirectory(File dir) {
        return dir.isDirectory() && dir.canRead() && dir.canExecute();
    }

    private static void printUsage() {
        System.err.println("usage: java -jar dupfinder.jar DIRS...");
        System.exit(1);
    }

    private static void printFileSet(Set<File> fileSet) {
        List<File> files = new ArrayList<>(fileSet);
        Collections.sort(files);

        for (File file : files) {
            System.out.println(file);
        }
        System.out.println();
    }
}

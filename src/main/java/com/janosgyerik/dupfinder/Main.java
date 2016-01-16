package com.janosgyerik.dupfinder;

import com.janosgyerik.dupfinder.dupfinder.DuplicateFileFinder;
import com.janosgyerik.dupfinder.dupfinder.DuplicateFileFinderImpl;
import com.janosgyerik.dupfinder.finder.FileFinder;
import com.janosgyerik.dupfinder.finder.FileFinderImpl;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("java -jar dupfinder.jar")
                .description("Find duplicate files in specified directory trees");
        parser.addArgument("dirs")
                .metavar("DIR")
                .type(String.class)
                .nargs("+")
                .help("path to a directory");
        parser.addArgument("--maxdepth")
                .metavar("N")
                .type(Integer.class)
                .help("descend at most N directory levels");
        parser.addArgument("--extension", "--ext")
                .type(String.class)
                .help("include only files matching extension");
        try {
            Namespace res = parser.parseArgs(args);
            main(res);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
    }

    private static void main(Namespace res) {
        FileFinder finder = new FileFinderImpl();
        DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinderImpl();

        for (Object arg : res.getList("dirs")) {
            File dir = new File(String.valueOf(arg));
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

    private static void printFileSet(Set<File> fileSet) {
        List<File> files = new ArrayList<>(fileSet);
        Collections.sort(files);

        for (File file : files) {
            System.out.println(file);
        }
        System.out.println();
    }
}

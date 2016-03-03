package com.janosgyerik.dupfinder;

import com.janosgyerik.dupfinder.dupfinder.DuplicateFileFinder;
import com.janosgyerik.dupfinder.finder.FileFinder;
import com.janosgyerik.dupfinder.finder.FileFinderImpl;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.janosgyerik.dupfinder.FileFilters.byExtension;
import static com.janosgyerik.dupfinder.FileFilters.byMaxDepth;
import static com.janosgyerik.dupfinder.FileFilters.composite;

public class Main {

    public static final String PARAM_DIRS = "dirs";

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("java -jar dupfinder.jar")
                .description("Find duplicate files in specified directory trees");
        parser.addArgument(PARAM_DIRS)
                .metavar("DIR")
                .type(String.class)
                .nargs("+")
                .help("path to a directory");
        parser.addArgument("--maxdepth", "-d")
                .metavar("N")
                .type(Integer.class)
                .help("descend at most N directory levels");
        parser.addArgument("--extension", "--ext", "-x")
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
        DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinder();
        FileFinder finder = new FileFinderImpl();

        for (Object arg : res.getList(PARAM_DIRS)) {
            File basedir = new File(String.valueOf(arg));
            if (isAccessibleDirectory(basedir)) {
                List<File> files = finder.find(basedir, createFileFilter(res, basedir));
                Set<Set<File>> duplicateFileSets = duplicateFileFinder.findDuplicates(files);
                duplicateFileSets.forEach(Main::printFileSet);
            }
        }
    }

    private static FileFilter createFileFilter(Namespace res, File basedir) {
        List<FileFilter> fileFilters = new ArrayList<>();

        String extension = res.getString("extension");
        if (extension != null) {
            fileFilters.add(byExtension(extension));
        }

        Integer depth = res.getInt("maxdepth");
        if (depth != null) {
            fileFilters.add(byMaxDepth(basedir, depth));
        }
        return composite(fileFilters.toArray(new FileFilter[fileFilters.size()]));
    }

    private static boolean isAccessibleDirectory(File dir) {
        return dir.isDirectory() && dir.canRead() && dir.canExecute();
    }

    private static void printFileSet(Set<File> fileSet) {
        List<File> files = new ArrayList<>(fileSet);
        Collections.sort(files);

        files.forEach(System.out::println);
        System.out.println();
    }
}

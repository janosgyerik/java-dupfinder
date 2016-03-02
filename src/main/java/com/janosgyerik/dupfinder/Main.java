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

    private static FileFinder finder = new FileFinderImpl();

    private interface FilterStrategy {
        List<File> find(File dir);
    }

    private static class FilterNothing implements FilterStrategy {
        @Override
        public List<File> find(File dir) {
            return finder.find(dir);
        }
    }

    private static class FilterByExtension implements FilterStrategy {
        private final FileFilter filter;

        private FilterByExtension(String extension) {
            this.filter = FileFilters.byExtension(extension);
        }

        @Override
        public List<File> find(File dir) {
            return finder.find(dir, filter);
        }
    }

    private static class FilterByDepth implements FilterStrategy {
        private final int depth;

        private FilterByDepth(int depth) {
            this.depth = depth;
        }

        @Override
        public List<File> find(File dir) {
            return finder.find(dir, depth);
        }
    }

    private static class FilterByExtensionAndDepth implements FilterStrategy {
        private final FileFilter filter;
        private final int depth;

        private FilterByExtensionAndDepth(String extension, int depth) {
            this.filter = FileFilters.byExtension(extension);
            this.depth = depth;
        }

        @Override
        public List<File> find(File dir) {
            return finder.find(dir, filter, depth);
        }
    }

    private static void main(Namespace res) {
        DuplicateFileFinder duplicateFileFinder = new DuplicateFileFinder();
        FilterStrategy strategy = getFilterStrategy(res);

        for (Object arg : res.getList(PARAM_DIRS)) {
            File dir = new File(String.valueOf(arg));
            if (isAccessibleDirectory(dir)) {
                List<File> files = strategy.find(dir);
                Set<Set<File>> duplicateFileSets = duplicateFileFinder.findDuplicates(files);
                duplicateFileSets.forEach(Main::printFileSet);
            }
        }
    }

    private static FilterStrategy getFilterStrategy(Namespace res) {
        String extension = res.getString("extension");
        Integer depth = res.getInt("maxdepth");

        if (extension != null && depth != null) {
            return new FilterByExtensionAndDepth(extension, depth);
        }
        if (extension != null) {
            return new FilterByExtension(extension);
        }
        if (depth != null) {
            return new FilterByDepth(depth);
        }
        return new FilterNothing();
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

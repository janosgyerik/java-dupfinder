# java-dupfinder

Find duplicate files in specified directory trees.

Building
--------

Create the JAR including dependencies using Maven:

    mvn clean compile assembly:single

Or simply run:

    ./build.sh

This will create `target/dupfinder-jar-with-dependencies.jar`,
an executable jar.

Usage
-----

To find duplicate files in the current directory and all sub-directories:

    java -jar $JAR .

To see the available options, use the `-h` or `--help` flag:

    java -jar $JAR --help

To find duplicate files in multiple directory trees,
only considering filenames with extension `.avi`,
descending to at most 2 sub-directory levels:

    java -jar $JAR --ext avi --maxdepth 2 path/to/dir path/to/other/dir

Or using the helper script:

    ./run.sh --ext avi --maxdepth 2 path/to/dir path/to/other/dir

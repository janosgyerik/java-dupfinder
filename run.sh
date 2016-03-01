#!/bin/sh

cd $(dirname "$0")

JAR=target/dupfinder-jar-with-dependencies.jar
java -jar $JAR "$@"

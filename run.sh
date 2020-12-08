#!/bin/bash
mkdir -p out
javac -d out $(find ./src/ -name "*.java")
java -cp ./out com.addorb.aoc2020.Main

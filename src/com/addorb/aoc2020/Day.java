package com.addorb.aoc2020;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Day {

    private static final boolean MEASURE_PERFORMANCE = true;

    public final List<String> input;

    public Day(String filename) {
        input = Collections.unmodifiableList(parseInput(filename));
        System.out.println(getClass().getSimpleName());
    }

    public final void run() {
        if (MEASURE_PERFORMANCE) {
            long start = System.currentTimeMillis();
            Object part1Result = part1();
            long p1 = System.currentTimeMillis();
            Object part2Result = part2();
            long p2 = System.currentTimeMillis();
            part1Answer(part1Result + ", took " + (p1-start) + " ms.");
            part2Answer(part2Result + ", took " + (p2-p1) + " ms.");
        } else {
            part1Answer(part1());
            part2Answer(part2());
        }
    }

    protected abstract Object part1();

    protected abstract Object part2();

    protected void part1Answer(Object answer) {
        System.out.println("Part 1, answer: " + answer.toString());
    }

    protected void part2Answer(Object answer) {
        System.out.println("Part 2, answer: " + answer.toString());
    }

    private List<String> parseInput(String filename) {
        List<String> lines = new ArrayList<>();
        File inputFile = new File(filename);
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    lines.add(line);
                }
            } catch (IOException e) {
                System.err.println("File reading failed.");
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found : " + filename);
            System.exit(1);
        }
        return lines;
    }
}

package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 extends Day {

    public Day6() {
        super("input/input-day-6.txt");
    }

    @Override
    protected Object part1() {
        List<Set<Character>> parsedList = parseDataPart1();
        int count = 0;
        for (Set<Character> yes : parsedList) {
            count += yes.size();
        }
        return count;
    }

    @Override
    protected Object part2() {
        List<Set<Character>> parsedList = parseDataPart2();
        int count = 0;
        for (Set<Character> yes : parsedList) {
            count += yes.size();
        }
        return count;
    }

    private List<Set<Character>> parseDataPart1() {
        List<Set<Character>> parsedList = new ArrayList<>();
        Set<Character> entry = new HashSet<>();
        for (String row : input) {
            if (row.isEmpty()) {
                parsedList.add(entry);
                entry = new HashSet<>();
                continue;
            }
            for (int i = 0; i < row.length(); i++) {
                entry.add(row.charAt(i));
            }
        }
        parsedList.add(entry);
        return parsedList;
    }

    private List<Set<Character>> parseDataPart2() {
        List<Set<Character>> parsedList = new ArrayList<>();
        List<Set<Character>> group = new ArrayList<>();
        for (String row : input) {
            if (row.isEmpty()) {
                parsedList.add(evaluateGroup(group));
                group = new ArrayList<>();
                continue;
            }
            Set<Character> entry = new HashSet<>();
            for (int i = 0; i < row.length(); i++) {
                entry.add(row.charAt(i));
            }
            group.add(entry);
        }
        parsedList.add(evaluateGroup(group));
        return parsedList;
    }

    private Set<Character> evaluateGroup(List<Set<Character>> group) {
        Set<Character> unanimous = new HashSet<>();
        Set<Character> first = group.get(0);
        for (Character question : first) {
            boolean sameAnswers = true;
            for (int i = 1; i < group.size(); i++) {
                Set<Character> other = group.get(i);
                if (!other.contains(question)) {
                    sameAnswers = false;
                    break;
                }
            }
            if (sameAnswers) {
                unanimous.add(question);
            }
        }
        return unanimous;
    }

}

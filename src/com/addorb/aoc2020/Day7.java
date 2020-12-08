package com.addorb.aoc2020;

import java.util.HashMap;
import java.util.Map;

public class Day7 extends Day {

    private final Map<String, Map<String, Integer>> rules;

    public Day7() {
        super("input/input-day-7.txt");
        rules = parseInput();
    }

    @Override
    protected Object part1() {
        int validColors = 0;
        for (Map.Entry<String, Map<String, Integer>> entry : rules.entrySet()) {
            if (containsShiny(rules, entry.getKey())) {
                validColors++;
            }
        }
        return validColors;
    }

    @Override
    protected Object part2() {
        return countBagsInRule(rules, "shiny gold");
    }

    private boolean containsShiny(Map<String, Map<String, Integer>> rules, String color) {
        Map<String, Integer> contents = rules.get(color);
        for (Map.Entry<String, Integer> content : contents.entrySet()) {
            String contentColor = content.getKey();
            if (contentColor.equals("shiny gold")) {
                return true;
            } else if (containsShiny(rules, contentColor)) {
                return true;
            }
        }

        // nothing in bag
        return false;
    }

    private int countBagsInRule(Map<String, Map<String, Integer>> rules, String color) {
        Map<String, Integer> contents = rules.get(color);
        int bagsInRule = 0;
        for (Map.Entry<String, Integer> content : contents.entrySet()) {
            int amountPerColor = content.getValue();
            bagsInRule += amountPerColor + amountPerColor * countBagsInRule(rules, content.getKey());
        }

        return bagsInRule;
    }

    private Map<String, Map<String, Integer>> parseInput() {
        Map<String, Map<String, Integer>> bags = new HashMap<>();
        for (String row : input) {
            String[] tokens = row.split(" ");
            String color = tokens[0] + " " + tokens[1];
            if (tokens[4].equals("no")) {
                bags.put(color, new HashMap<>());
            } else {
                HashMap<String, Integer> contents = new HashMap<>();
                int i = 4;
                while (i < tokens.length) {
                    int count = Integer.parseInt(tokens[i]);
                    String containedColor = tokens[i + 1] + " " + tokens[i + 2];
                    i += 4;
                    contents.put(containedColor, count);
                }
                bags.put(color, contents);
            }
        }
        return bags;
    }
}

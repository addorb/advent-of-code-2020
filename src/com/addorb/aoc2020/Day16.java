package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day16 extends Day {

    private final List<Rule> rules = new ArrayList<>();
    private final List<Integer> myTicket = new ArrayList<>();
    private final List<List<Integer>> nearbyTickets = new ArrayList<>();

    public Day16() {
        super("input/input-day-16.txt");

        parseInput(rules, myTicket, nearbyTickets);
    }


    @Override
    protected Object part1() {
        int sum = 0;
        for (List<Integer> ticket : nearbyTickets) {
            for (int field : ticket) {
                boolean validField = false;
                for (Rule rule : rules) {
                    if (rule.range1Low <= field && field <= rule.range1High ||
                            rule.range2Low <= field && field <= rule.range2High) {
                        validField = true;
                        break;
                    }
                }
                if (!validField) {
                    sum+=field;
                }
            }
        }
        return sum;
    }

    @Override
    protected Object part2() {
        List<List<Integer>> validTickets = new ArrayList<>();
        for (List<Integer> ticket : nearbyTickets) {
            boolean validFields = true;
            for (int field : ticket) {
                boolean validField = false;
                for (Rule rule : rules) {
                    if (rule.isFieldValid(field)) {
                        validField = true;
                        break;
                    }
                }
                if (!validField) {
                    validFields = false;
                    break;
                }
            }
            if (validFields) {
                validTickets.add(ticket);
            }
        }
        final int ticketLength = validTickets.get(0).size();

        Map<Integer, Rule> fieldMap = new HashMap<>();
        while (!rules.isEmpty()) {
            for (int i = 0; i < ticketLength; i++) {
                List<Rule> fieldValidForRules = new ArrayList<>();
                for (Rule rule : rules) {
                    boolean allFieldsValidForRule = true;
                    for (List<Integer> ticket : validTickets) {
                        int field = ticket.get(i);
                        if (!rule.isFieldValid(field)) {
                            allFieldsValidForRule = false;
                            break;
                        }
                    }
                    if (allFieldsValidForRule) {
                        fieldValidForRules.add(rule);
                    }
                }
                if (fieldValidForRules.size() == 1) {
                    Rule foundRule = fieldValidForRules.get(0);
                    rules.remove(foundRule);
                    fieldMap.put(i, foundRule);
                }
            }
        }
        long product = 1;
        for (int i = 0; i < myTicket.size(); i++) {
            int field = myTicket.get(i);
            Rule rule = fieldMap.get(i);
            if (rule.name.startsWith("departure")) {
                product *= field;
            }
        }
        return product;
    }

    private void parseInput(List<Rule> rules, List<Integer> myTicket, List<List<Integer>> nearbyTickets) {
        int parsePhase = 0;
        for (String row : input) {
            switch (parsePhase) {
                case 0 -> {
                    if (row.isEmpty()) {
                        parsePhase++;
                    } else {
                        String[] tokens = row.split(": ");
                        String[] ranges = tokens[1].split(" or ");
                        String[] range1 = ranges[0].split("-");
                        String[] range2 = ranges[1].split("-");
                        rules.add(new Rule(tokens[0], Integer.parseInt(range1[0]), Integer.parseInt(range1[1]),
                                Integer.parseInt(range2[0]), Integer.parseInt(range2[1])));
                    }
                }
                case 1 -> {
                    if (row.isEmpty()) {
                        parsePhase++;
                    } else if (!row.equals("your ticket:")) {
                        for (String ticketData : row.split(",")) {
                            myTicket.add(Integer.parseInt(ticketData));
                        }
                    }
                }
                case 2 -> {
                    if (!row.equals("nearby tickets:")) {
                        List<Integer> nearbyTicket = new ArrayList<>();
                        for (String ticketData : row.split(",")) {
                            nearbyTicket.add(Integer.parseInt(ticketData));
                        }
                        nearbyTickets.add(nearbyTicket);
                    }
                }
            }
        }
    }

    private static class Rule {
        private final String name;
        private final int range1Low;
        private final int range1High;
        private final int range2Low;
        private final int range2High;

        private Rule(String name, int range1Low, int range1High, int range2Low, int range2High) {
            this.name = name;
            this.range1Low = range1Low;
            this.range1High = range1High;
            this.range2Low = range2Low;
            this.range2High = range2High;
        }

        private boolean isFieldValid(int field) {
            return range1Low <= field && field <= range1High || range2Low <= field && field <= range2High;
        }

    }
}

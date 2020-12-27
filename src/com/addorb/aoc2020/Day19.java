package com.addorb.aoc2020;

import java.util.*;
import java.util.stream.Collectors;

public class Day19 extends Day {

    private final Map<Integer, Rule> rulesMap = new HashMap<>();
    private final List<String> messages = new ArrayList<>();

    public Day19() {
        super("input/input-day-19.txt");
        parseInput();
    }

    @Override
    protected Object part1() {
        int validMessages = 0;
        for (String msg : messages) {
            Rule rule = rulesMap.get(0);
            Message message = new Message(msg);
            if (rule.evaluate(message, 0) >= 0 && message.fullyValidated()) {
                validMessages++;
            }
        }
        return validMessages;
    }

    @Override
    protected Object part2() {
        Rule rule42 = rulesMap.get(42);
        Rule rule31 = rulesMap.get(31);

        List<String> rule42messages = findMessageMatchingRule(rule42);
        List<String> rule31messages = findMessageMatchingRule(rule31);
        for (String rule42message : rule42messages) {
            if (rule31messages.contains(rule42message)) {
                throw new RuntimeException("Rule42 and rule31 have overlapping matches, the " +
                        "following algorithm does not support it");
            }
        }

        final int chunkLength = rule42messages.get(0).length();
        int validMessages = 0;
        for (String row : messages) {
            int match42Count = 0;
            int match31Count = 0;
            final int chunkCount = row.length() / chunkLength;
            int front = 0;
            int back = row.length() - chunkLength;
            String chunk;
            while (back >= front) {
                chunk = row.substring(back, back + chunkLength);
                if (rule31messages.contains(chunk)) {
                    back -= chunkLength;
                    match31Count++;
                } else {
                    break;
                }
            }
            if (match31Count > chunkCount / 2 || match31Count == 0) {
                // Need at least 1 rule31 match, and rule31-matches must be less than
                // rule42-matches.
                continue;
            }

            while (front < row.length()) {
                chunk = row.substring(front, front + chunkLength);
                if (rule42messages.contains(chunk)) {
                    front += chunkLength;
                    match42Count++;
                } else {
                    break;
                }
            }

            if (front >= back && match31Count < match42Count) {
                validMessages++;
            }
        }
        return validMessages;
    }

    private List<String> findMessageMatchingRule(Rule rule) {
        List<String> possibleMatches = new ArrayList<>();
        int messageLength = 1;
        boolean tooLongMessages = false;
        while (!tooLongMessages) {
            long iterations = Math.round(Math.pow(2, messageLength));
            int longMessageCount = 0;
            for (long i = 0; i < iterations; i++) {
                String bits = Long.toBinaryString(i).replaceAll("0", "a")
                        .replaceAll("1", "b");
                long paddingRequired = Long.toBinaryString(iterations - 1).length() - bits.length();
                StringBuilder paddedString = new StringBuilder();
                for (int p = 0; p < paddingRequired; p++) {
                    paddedString.append('a');
                }
                paddedString.append(bits);
                Message message = new Message(paddedString.toString());
                try {
                    if (rule.evaluate(message, 0) > 0) {
                        if (message.fullyValidated()) {
                            possibleMatches.add(message.message);
                        } else {
                            longMessageCount++;
                        }
                    }
                } catch (Exception e) {
                    // string too short for rule, not valid
                }
            }
            if (longMessageCount != 0) {
                tooLongMessages = true;
            }
            messageLength++;
        }
        return possibleMatches;
    }

    private void parseInput() {
        boolean parsingRules = true;
        for (String row : input) {
            if (row.isEmpty()) {
                parsingRules = false;
                continue;
            }
            if (parsingRules) {
                parseRule(row);
            } else {
                messages.add(row);
            }
        }
    }

    private void parseRule(String rule) {
        String[] tokens = rule.split(": ");
        int ruleId = Integer.parseInt(tokens[0]);

        if (tokens[1].charAt(0) == '\"') {
            rulesMap.put(ruleId, new CharacterRule(tokens[1].charAt(1)));
        } else if (tokens[1].contains("|")) {
            String[] lists = tokens[1].split(" \\| ");
            List<Rule> orRules = new ArrayList<>();
            for (String list : lists) {
                orRules.add(new ListRule(Arrays.stream(list.split(" "))
                        .map(Integer::parseInt).collect(Collectors.toList())));
            }
            rulesMap.put(ruleId, new AtLeastOneRule(orRules));

        } else {
            rulesMap.put(ruleId, new ListRule(Arrays.stream(tokens[1].split(" "))
                    .map(Integer::parseInt).collect(Collectors.toList())));
        }
    }

    private abstract static class Rule {
        abstract int evaluate(Message message, int index);
    }

    private static class CharacterRule extends Rule {
        private final char character;

        public CharacterRule(char character) {
            this.character = character;
        }

        @Override
        int evaluate(Message message, int index) {
            int result = (message.message.charAt(index) == character) ? (index + 1) : -1;
            message.visited[index] = true;
            return result;
        }
    }

    private static class AtLeastOneRule extends Rule {
        private final List<Rule> rules;

        public AtLeastOneRule(List<Rule> rules) {
            this.rules = rules;
        }

        @Override
        int evaluate(Message message, int index) {
            for (Rule rule : rules) {
                int result = rule.evaluate(message, index);
                if (result >= 0) {
                    return result;
                }
            }
            return -1;
        }
    }

    private static class Message {
        String message;
        boolean[] visited;

        private Message(String message) {
            this.message = message;
            visited = new boolean[message.length()];
            Arrays.fill(visited, false);
        }

        private boolean fullyValidated() {
            for (Boolean character : visited) {
                if (!character) {
                    return false;
                }
            }
            return true;
        }
    }

    private class ListRule extends Rule {
        private final List<Integer> rules;

        public ListRule(List<Integer> rules) {
            this.rules = rules;
        }

        @Override
        int evaluate(Message message, int index) {
            for (Integer rule : rules) {
                index = rulesMap.get(rule).evaluate(message, index);
                if (index == -1) {
                    return -1;
                }
            }
            return index;
        }
    }
}

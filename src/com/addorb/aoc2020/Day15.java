package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 extends Day {

    private final List<Integer> startingNumbers;

    public Day15() {
        super("input/input-day-15.txt");
        startingNumbers = parseStartingNumbers();
    }

    @Override
    protected Object part1() {
        return play(2020);
    }

    @Override
    protected Object part2() {
        return play(30000000);
    }

    private int play(int rounds) {
        Map<Integer, SpokenNumber> numberHistory = new HashMap<>();
        int lastNumberSpoken = 0;
        int turn = 1;
        for (Integer startingNumber : startingNumbers) {
            numberHistory.put(startingNumber, new SpokenNumber(turn));
            lastNumberSpoken = startingNumber;
            turn++;
        }

        while (turn <= rounds) {
            SpokenNumber previous = numberHistory.get(lastNumberSpoken);

            if (previous.count == 1) {
                lastNumberSpoken = 0;
            } else {
                lastNumberSpoken = turn - 1 - previous.secondToLastMention;
            }
            SpokenNumber spoken = numberHistory.get(lastNumberSpoken);
            if (spoken == null) {
                numberHistory.put(lastNumberSpoken, new SpokenNumber(turn));
            } else {
                spoken.secondToLastMention = spoken.lastMention;
                spoken.lastMention = turn ;
                spoken.count++;
            }

            turn++;
        }
        return lastNumberSpoken;
    }

    private List<Integer> parseStartingNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (String number : input.get(0).split(",")) {
            numbers.add(Integer.parseInt(number));
        }
        return numbers;
    }

    private static final class SpokenNumber {
        private int lastMention;
        private int secondToLastMention = 0;
        private int count = 1;

        private SpokenNumber(int lastTurn) {
            this.lastMention = lastTurn;
        }
    }
}

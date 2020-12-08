package com.addorb.aoc2020;

public final class Day1 extends Day {

    private final int[] inputInts;

    public Day1() {
        super("input/input-day-1.txt");
        inputInts = Utils.convertInputToIntArray(input);
    }

    @Override
    protected Object part1() {
        final int count = inputInts.length;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (inputInts[i] + inputInts[j] == 2020) {
                    return inputInts[i] * inputInts[j];
                }
            }
        }
        return -1;
    }

    @Override
    protected Object part2() {
        final int count = inputInts.length;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                for (int k = j + 1; k < count; k++) {
                    if (inputInts[i] + inputInts[j] + inputInts[k] == 2020) {
                        return inputInts[i] * inputInts[j] * inputInts[k];
                    }
                }
            }
        }
        return -1;
    }
}

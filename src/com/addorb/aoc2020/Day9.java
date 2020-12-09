package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.List;

public class Day9 extends Day {

    final long[] data;

    public Day9() {
        super("input/input-day-9.txt");
        data = Utils.convertInputToLongArray(input);
    }

    @Override
    protected Object part1() {
        return findNumber(25);
    }

    @Override
    protected Object part2() {
        return findWeakness(findNumber(25));
    }

    private long findNumber(int window) {
        for (int i = window; i < data.length; i++) {
            boolean sumFound = false;
            for (int j = i - window; j < i; j++) {
                for (int k = j + 1; k < i; k++) {
                    if (data[i] == data[j] + data[k]) {
                        sumFound = true;
                    }
                }
            }
            if (!sumFound) {
                return data[i];
            }
        }
        return -1;
    }

    private long findWeakness(final long number) {
        List<Long> range = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            int sum = 0;
            range.clear();
            for (int j = i; sum <= number; j++) {
                sum += data[j];
                range.add(data[j]);
                if (sum == number) {
                    range.sort(Long::compareTo);
                    return range.get(0) + range.get(range.size() - 1);
                }
            }
        }
        return -1;
    }
}

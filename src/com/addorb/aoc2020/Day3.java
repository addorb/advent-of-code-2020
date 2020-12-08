package com.addorb.aoc2020;

import java.util.Iterator;

public class Day3 extends Day {

    public Day3() {
        super("input/input-day-3.txt");
    }

    @Override
    protected Object part1() {
        return ride(3, 1);
    }

    @Override
    protected Object part2() {
        int ride1 = ride(1, 1);
        int ride2 = ride(3, 1);
        int ride3 = ride(5, 1);
        int ride4 = ride(7, 1);
        int ride5 = ride(1, 2);

        return ride1 * ride2 * ride3 * ride4 * ride5;
    }

    private int ride(int right, int down) {
        int treesFound = input.get(0).charAt(0) == '#' ? 1 : 0;
        int x = 0;
        Iterator<String> rows = input.iterator();
        String currentRow = rows.next();
        while (rows.hasNext()) {
            for (int i = 0; i < right; i++) {
                x++;
                if (x >= currentRow.length()) {
                    x = 0;
                }
            }
            boolean stillInForest = true;
            for (int i = 0; i < down; i++) {
                if (rows.hasNext()) {
                    currentRow = rows.next();
                } else {
                    stillInForest = false;
                }
            }

            if (stillInForest && currentRow.charAt(x) == '#') {
                treesFound++;
            }
        }
        return treesFound;
    }

}

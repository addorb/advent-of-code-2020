package com.addorb.aoc2020;

import java.util.List;

public class Utils {

    public static int[] convertInputToIntArray(List<String> input) {
        int[] converted = new int[input.size()];
        for (int i = 0; i < input.size(); i++) {
            converted[i] = Integer.parseInt(input.get(i));
        }
        return converted;
    }
}

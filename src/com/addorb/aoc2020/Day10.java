package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day10 extends Day {

    final List<Integer> adapters;

    public Day10() {
        super("input/input-day-10.txt");
        adapters = Utils.convertInputToIntList(input);
        adapters.sort(Integer::compareTo);
    }

    @Override
    protected Object part1() {
        Map<Integer, Integer> joltSteps = new HashMap<>();
        joltSteps.put(1, 0);
        joltSteps.put(2, 0);
        joltSteps.put(3, 0);
        int currentJoltage = 0;
        for (Integer adapter : adapters) {
            int step = adapter - currentJoltage;
            joltSteps.put(step, joltSteps.get(step) + 1);
            currentJoltage = adapter;
        }

        joltSteps.put(3, joltSteps.get(3) + 1);
        return joltSteps.get(1) * joltSteps.get(3);
    }


    @Override
    protected Object part2() {
        // There are multiple possibilities to start with from the charging outlet,
        // let the charging outlet be the starting point.
        adapters.add(0, 0);
        return findCombinations(0, new HashMap<>());
    }

    private long findCombinations(int adapterIndex, Map<Integer, Long> resultCache) {
        List<Integer> adaptersToConnect = new ArrayList<>();
        if (adapterIndex == adapters.size() - 1) {
            // Last adapter reached, this combination is done
            return 1L;
        }
        if (resultCache.containsKey(adapterIndex)) {
            return resultCache.get(adapterIndex);
        }
        int startingAdapter = adapters.get(adapterIndex);
        int i = adapterIndex + 1;
        while (i < adapters.size() && adapters.get(i) - startingAdapter <= 3) {
            adaptersToConnect.add(i);
            i++;
        }
        long combinations = 0;
        for (Integer toConnect : adaptersToConnect) {
            combinations += findCombinations(toConnect, resultCache);
        }
        adaptersToConnect.clear();
        resultCache.put(adapterIndex, combinations);
        return combinations;
    }
}

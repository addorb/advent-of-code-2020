package com.addorb.aoc2020;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Day5 extends Day {

    final List<Integer> seatIds;

    public Day5() {
        super("input/input-day-5.txt");
        seatIds = parseSeatIds();
    }

    @Override
    protected Object part1() {
        return seatIds.get(seatIds.size() - 1);
    }

    @Override
    protected Object part2() {
        int mySeatId = -1;
        for (int i = 1; i < seatIds.size(); i++) {
            if ((seatIds.get(i) - seatIds.get(i - 1)) > 1) {
                mySeatId = seatIds.get(i) - 1;
            }
        }
        return mySeatId;
    }

    private List<Integer> parseSeatIds() {
        List<Integer> seatIds = new ArrayList<>(input.size());
        for (String pass : input) {
            Queue<Character> rowData = new ArrayDeque<>(7);
            for (int i = 0; i < 7; i++) {
                rowData.add(pass.charAt(i));
            }
            int row = partitionRow(rowData, 0, 127);
            Queue<Character> seatData = new ArrayDeque<>(3);
            for (int i = 7; i < 10; i++) {
                seatData.add(pass.charAt(i));
            }
            int seat = partitionSeat(seatData, 0, 7);
            seatIds.add(row * 8 + seat);
        }
        seatIds.sort(Integer::compareTo);
        return seatIds;
    }

    private int partitionRow(Queue<Character> rowData, int low, int high) {
        Character character = rowData.remove();
        if (rowData.isEmpty()) {
            return character == 'F' ? low : high;
        } else {
            if (character == 'F') {
                return partitionRow(rowData, low, low + (high - low) / 2);
            } else {
                return partitionRow(rowData, high - (high - low) / 2, high);
            }
        }
    }

    private int partitionSeat(Queue<Character> seatData, int low, int high) {
        Character character = seatData.remove();
        if (seatData.isEmpty()) {
            return character == 'L' ? low : high;
        } else {
            if (character == 'L') {
                return partitionSeat(seatData, low, low + (high - low) / 2);
            } else {
                return partitionSeat(seatData, high - (high - low) / 2, high);
            }
        }
    }
}

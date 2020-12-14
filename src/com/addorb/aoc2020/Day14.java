package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 extends Day {

    private static final int WRITE_MASK_ACTION = 0;
    private static final int WRITE_MEM_ACTION = 1;

    private final List<Instruction> instructions;

    private final Map<Long, Long> memory = new HashMap<>();

    public Day14() {
        super("input/input-day-14.txt");
        instructions = parseData();
    }

    @Override
    protected Object part1() {
        long setFlagsMask = 0;
        long clearFlagsMask = 0;
        for (Instruction instruction : instructions) {
            if (instruction.action == WRITE_MASK_ACTION) {
                String mask = instruction.value;
                StringBuilder setMaskBuilder = new StringBuilder();
                StringBuilder clearMaskBuilder = new StringBuilder();
                for (int i = 0; i < mask.length(); i++) {
                    setMaskBuilder.append(mask.charAt(i) == '1' ? '1' : '0');
                    clearMaskBuilder.append(mask.charAt(i) == '0' ? '0' : '1');
                }
                setFlagsMask = Long.parseLong(setMaskBuilder.toString(), 2);
                clearFlagsMask = Long.parseLong(clearMaskBuilder.toString(), 2);
            } else {
                String[] tokens = instruction.value.split(",");
                long address = Long.parseLong(tokens[0]);
                long value = Long.parseLong(tokens[1]);
                value |= setFlagsMask;
                value &= clearFlagsMask;
                memory.put(address, value);
            }
        }
        long sum = 0;
        for (Long data : memory.values()) {
            sum += data;
        }
        return sum;
    }

    @Override
    protected Object part2() {
        memory.clear();
        long setFlagsMask = 0;
        List<Long> floatingPositionMasks = new ArrayList<>();
        for (Instruction instruction : instructions) {
            if (instruction.action == WRITE_MASK_ACTION) {
                String mask = instruction.value;
                StringBuilder setMaskBuilder = new StringBuilder();
                floatingPositionMasks = new ArrayList<>();
                for (int i = 0; i < mask.length(); i++) {
                    setMaskBuilder.append(mask.charAt(i) == '1' ? '1' : '0');
                    if (mask.charAt(i) == 'X') {
                        floatingPositionMasks.add(Math.round(Math.pow(2, mask.length() - 1 - i)));
                    }
                }
                setFlagsMask = Long.parseLong(setMaskBuilder.toString(), 2);
            } else {
                String[] tokens = instruction.value.split(",");
                long address = Long.parseLong(tokens[0]);
                long value = Long.parseLong(tokens[1]);

                long iterations = Math.round(Math.pow(2, floatingPositionMasks.size()));
                for (long i = 0; i < iterations; i++) {
                    // bit positions in i, become the index in floatingPositionMasks
                    String bits = Long.toBinaryString(i);
                    long paddingRequired = Long.toBinaryString(iterations - 1).length() - bits.length();
                    StringBuilder paddedString = new StringBuilder();
                    for (int p = 0; p < paddingRequired; p++) {
                        paddedString.append('0');
                    }
                    paddedString.append(bits);
                    bits = paddedString.toString();
                    address |= setFlagsMask;

                    for (int j = 0; j < bits.length(); j++) {
                        if (bits.charAt(j) == '0') {
                            address &= ~floatingPositionMasks.get(j);
                        } else {
                            address |= floatingPositionMasks.get(j);
                        }
                    }
                    memory.put(address, value);
                }
            }
        }
        long sum = 0;
        for (Long data : memory.values()) {
            sum += data;
        }
        return sum;
    }

    private List<Instruction> parseData() {
        List<Instruction> parsed = new ArrayList<>();
        for (String row : input) {
            String[] tokens = row.split(" = ");
            if (tokens[0].equals("mask")) {
                parsed.add(new Instruction(WRITE_MASK_ACTION, tokens[1]));
            } else {
                final String address = tokens[0];
                long value = Long.parseLong(address.substring(address.indexOf('[') + 1, address.indexOf(']')));
                parsed.add(new Instruction(WRITE_MEM_ACTION, value + "," + Long.parseLong(tokens[1])));
            }
        }
        return parsed;
    }

    private static class Instruction {
        final int action;
        final String value;

        private Instruction(int action, String value) {
            this.action = action;
            this.value = value;
        }
    }
}

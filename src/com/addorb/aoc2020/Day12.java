package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.List;

public class Day12 extends Day {

    private static final char N = 'N';
    private static final char S = 'S';
    private static final char E = 'E';
    private static final char W = 'W';
    private static final char L = 'L';
    private static final char R = 'R';
    private static final char F = 'F';

    private final List<Instruction> instructions;

    public Day12() {
        super("input/input-day-12.txt");
        instructions = parseData();
    }

    @Override
    protected Object part1() {
        // LAT = north south (positive towards north, negative towards south)
        // LON = east west (positive towards east, negative towards west)
        // North = 0 degrees, east = 90 etc.
        int latitude = 0;
        int longitude = 0;
        int direction = 90;
        for (Instruction instruction : instructions) {

            switch (instruction.action) {
                case N -> latitude += instruction.value;
                case S -> latitude -= instruction.value;
                case E -> longitude += instruction.value;
                case W -> longitude -= instruction.value;
                case L -> direction = adjustDirection(direction, -instruction.value);
                case R -> direction = adjustDirection(direction, instruction.value);
                case F -> {
                    switch (direction) {
                        case 0 -> latitude += instruction.value;
                        case 90 -> longitude += instruction.value;
                        case 180 -> latitude -= instruction.value;
                        case 270 -> longitude -= instruction.value;
                    }
                }
            }
        }
        return Math.abs(latitude) + Math.abs(longitude);
    }

    private int adjustDirection(int currentDirection, int change) {
        int newDirection = currentDirection + change;
        if (newDirection < 0) {
            return 360 + newDirection;
        }
        return newDirection % 360;
    }

    @Override
    protected Object part2() {
        // LAT = north south (positive towards north, negative towards south)
        // LON = east west (positive towards east, negative towards west)
        // North = 0 degrees, east = 90 etc.
        int latitude = 0;
        int longitude = 0;
        Waypoint waypoint = new Waypoint(1, 10);
        for (Instruction instruction : instructions) {
            switch (instruction.action) {
                case N -> waypoint.latitude += instruction.value;
                case S -> waypoint.latitude -= instruction.value;
                case E -> waypoint.longitude += instruction.value;
                case W -> waypoint.longitude -= instruction.value;
                case L -> waypoint.rotate(-instruction.value);
                case R -> waypoint.rotate(instruction.value);
                case F -> {
                    for (int i = 0; i < instruction.value; i++) {
                        latitude += waypoint.latitude;
                        longitude += waypoint.longitude;
                    }
                }
            }
        }
        return Math.abs(latitude) + Math.abs(longitude);
    }

    private List<Instruction> parseData() {
        List<Instruction> instructions = new ArrayList<>();
        for (String row : input) {
            instructions.add(new Instruction(row.charAt(0), Integer.parseInt(row.substring(1))));
        }
        return instructions;
    }

    private static class Waypoint {
        private int latitude;
        private int longitude;

        private Waypoint(int latitude, int longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        private void rotate(int degrees) {
            if (Math.abs(degrees) == 180) {
                latitude = -latitude;
                longitude = -longitude;
            } else if (degrees == -90 || degrees == 270) {
                int oldLat = latitude;
                latitude = longitude;
                longitude = -oldLat;
            } else {
                int oldLat = latitude;
                latitude = -longitude;
                longitude = oldLat;
            }
        }
    }

    private static class Instruction {
        final char action;
        final int value;

        private Instruction(char action, int value) {
            this.action = action;
            this.value = value;
        }
    }
}

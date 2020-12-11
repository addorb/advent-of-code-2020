package com.addorb.aoc2020;

public class Day11 extends Day {

    private static final char NO_CHAIR = '.';
    private static final char VACANT = 'L';
    private static final char OCCUPIED = '#';

    private final char[][] initial;
    private final int width;
    private final int height;

    private char[][] seats;
    private char[][] copy;

    public Day11() {
        super("input/input-day-11.txt");
        width = input.get(0).length();
        height = input.size();
        initial = parseInput(width, height);
    }

    @Override
    protected Object part1() {
        findEquilibrium(this::countNeighbours, 4);
        return countOccupied();
    }

    @Override
    protected Object part2() {
        findEquilibrium(this::countNeighboursWithLineOfSight, 5);
        return countOccupied();
    }

    private void findEquilibrium(NeighbourCounter counter, int maxNeighbours) {
        seats = copyMatrix(initial);
        boolean changesOccurred = true;
        while (changesOccurred) {
            copy = copyMatrix(seats);
            changesOccurred = movePeopleAround(counter, maxNeighbours);
            seats = copy;
        }
    }

    private char[][] copyMatrix(char[][] source) {
        char[][] copy = new char[width][height];
        for (int x = 0; x < width; x++) {
            System.arraycopy(source[x], 0, copy[x], 0, height);
        }
        return copy;
    }

    private int countOccupied() {
        int occupied = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isSeatTaken(x, y)) {
                    occupied++;
                }
            }
        }
        return occupied;
    }

    private boolean movePeopleAround(NeighbourCounter counter, int maxNeighbours) {
        boolean seatsChanged = false;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (seats[x][y] == NO_CHAIR) {
                    continue;
                }
                int neighbours = counter.countNeighbours(x, y);
                if (seats[x][y] == VACANT && neighbours == 0) {
                    copy[x][y] = OCCUPIED;
                    seatsChanged = true;
                } else if (seats[x][y] == OCCUPIED && neighbours >= maxNeighbours) {
                    copy[x][y] = VACANT;
                    seatsChanged = true;
                }
            }
        }
        return seatsChanged;
    }

    private int countNeighbours(int startX, int startY) {
        int neighbours = 0;
        for (int col = startX - 1; col <= startX + 1; col++) {
            for (int row = startY - 1; row <= startY + 1; row++) {
                if (col == startX && row == startY) {
                    continue;
                }
                if (isSeatTaken(col, row)) {
                    neighbours++;
                }
            }
        }
        return neighbours;
    }

    private boolean isSeatTaken(int x, int y) {
        try {
            return seats[x][y] == OCCUPIED;
        } catch (ArrayIndexOutOfBoundsException e) {
            // Allow checking outside of matrix to make life easier.
            return false;
        }
    }

    private int countNeighboursWithLineOfSight(int startX, int startY) {
        int neighbours = 0;
        for (int dirX = -1; dirX <= 1; dirX++) {
            for (int dirY = -1; dirY <= 1; dirY++) {
                if (dirX == 0 && dirY == 0) {
                    continue;
                }
                int x = startX;
                int y = startY;
                boolean edgeOrSeatFound = false;
                while (!edgeOrSeatFound) {
                    x += dirX;
                    y += dirY;
                    try {
                        char seat = seats[x][y];
                        if (seat == OCCUPIED) {
                            neighbours++;
                            edgeOrSeatFound = true;
                        } else if (seat == VACANT) {
                            edgeOrSeatFound = true;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        edgeOrSeatFound = true;
                    }
                }
            }
        }
        return neighbours;
    }

    private char[][] parseInput(int width, int height) {
        char[][] output = new char[width][height];
        for (int row = 0; row < height; row++) {
            String rowData = input.get(row);
            for (int col = 0; col < width; col++) {
                output[col][row] = rowData.charAt(col);
            }
        }
        return output;
    }

    private interface NeighbourCounter {
        int countNeighbours(int startX, int startY);
    }
}

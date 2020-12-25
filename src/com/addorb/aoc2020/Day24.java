package com.addorb.aoc2020;

import java.util.*;

public class Day24 extends Day {

    List<List<Direction>> directions;

    Set<Tile> blackTiles = new HashSet<>();

    public Day24() {
        super("input/input-day-24.txt");
        directions = parseInput();
    }

    @Override
    protected Object part1() {
        for (List<Direction> row : directions) {
            Position position = new Position(0, 0);
            for (Direction direction : row) {
                position.move(direction);
            }
            Tile tileForPosition = new Tile(position.x, position.y);
            if (blackTiles.contains(tileForPosition)) {
                blackTiles.remove(tileForPosition);
            } else {
                blackTiles.add(tileForPosition);
            }
        }
        return blackTiles.size();
    }

    @Override
    protected Object part2() {
        int generation = 1;
        final List<Direction> directionsToNeighbours = new ArrayList<>();
        directionsToNeighbours.add(Direction.NORTH_EAST);
        directionsToNeighbours.add(Direction.WEST);
        directionsToNeighbours.add(Direction.SOUTH_WEST);
        directionsToNeighbours.add(Direction.SOUTH_EAST);
        directionsToNeighbours.add(Direction.EAST);
        directionsToNeighbours.add(Direction.NORTH_EAST);
        while (generation <= 100) {
            Set<Tile> evaluationCandidates = new HashSet<>();
            for (Tile tile : blackTiles) {
                Position position = new Position(tile.x, tile.y);
                evaluationCandidates.add(new Tile(position.x, position.y));
                for (Direction direction : directionsToNeighbours) {
                    position.move(direction);
                    evaluationCandidates.add(new Tile(position.x, position.y));
                }
            }
            Set<Tile> newGeneration = new HashSet<>();
            for (Tile candidate : evaluationCandidates) {
                Position position = new Position(candidate.x, candidate.y);
                int neighbours = 0;
                for (Direction direction : directionsToNeighbours) {
                    position.move(direction);
                    if (blackTiles.contains(new Tile(position.x, position.y))) {
                        neighbours++;
                    }
                }
                if (blackTiles.contains(candidate)) {
                    if (neighbours > 0 && neighbours < 3) {
                        newGeneration.add(candidate);
                    }
                } else if (neighbours == 2) {
                    newGeneration.add(candidate);
                }
            }
            blackTiles = newGeneration;
            System.out.println("Day " + generation + ": " + blackTiles.size());
            generation++;
        }
        return blackTiles.size();
    }

    private List<List<Direction>> parseInput() {
        List<List<Direction>> result = new ArrayList<>();
        for (String row : input) {
            List<Direction> directionRow = new ArrayList<>();
            char northSouth = 0;
            for (int i = 0; i < row.length(); i++) {
                char character = row.charAt(i);
                switch (character) {
                    case 'e' -> {
                        if (northSouth == 0) {
                            directionRow.add(Direction.EAST);
                        } else if (northSouth == 's') {
                            directionRow.add(Direction.SOUTH_EAST);
                            northSouth = 0;
                        } else {
                            directionRow.add(Direction.NORTH_EAST);
                            northSouth = 0;
                        }
                    }
                    case 'w' -> {
                        if (northSouth == 0) {
                            directionRow.add(Direction.WEST);
                        } else if (northSouth == 's') {
                            directionRow.add(Direction.SOUTH_WEST);
                            northSouth = 0;
                        } else {
                            directionRow.add(Direction.NORTH_WEST);
                            northSouth = 0;
                        }
                    }
                    case 's', 'n' -> northSouth = character;
                }
            }
            result.add(directionRow);
        }
        return result;
    }

    private enum Direction {
        EAST,
        SOUTH_EAST,
        SOUTH_WEST,
        WEST,
        NORTH_EAST,
        NORTH_WEST
    }

    private static class Position {
        private int x;
        private int y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /*
         * How the x & y-coordinates correspond to a hexagonal grid.
         *
         *  *           *           *
         *  *  -1,-1    *    0,-1   *
         *  *           *           *
         *     *     *     *     *
         *        *           *
         *  -1,0  *    0,0    *  1,0
         *        *           *
         *     *     *     *     *
         *  *           *           *
         *  *  0,1      *    1,1    *
         *  *           *           *
         */

        private void move(Direction direction) {
            switch (direction) {
                case EAST -> x++;
                case WEST -> x--;
                case NORTH_EAST -> y--;
                case SOUTH_WEST -> y++;
                case NORTH_WEST -> {
                    x--;
                    y--;
                }
                case SOUTH_EAST -> {
                    x++;
                    y++;
                }
            }
        }
    }

    private static class Tile {
        private final int x;
        private final int y;

        private Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tile tile = (Tile) o;
            return x == tile.x && y == tile.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}

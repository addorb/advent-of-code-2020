package com.addorb.aoc2020;

import java.util.*;

public class Day17 extends Day {

    public Day17() {
        super("input/input-day-17.txt");
    }

    @Override
    protected Object part1() {
        Set<Cube> activeCubes = parseInputPart1();
        int generation = 1;
        while (generation <= 6) {
            Set<Cube> evaluationCandidates = new HashSet<>();
            for (Cube cube : activeCubes) {
                for (int x = cube.x - 1; x <= cube.x + 1; x++) {
                    for (int y = cube.y - 1; y <= cube.y + 1; y++) {
                        for (int z = cube.z - 1; z <= cube.z + 1; z++) {
                            evaluationCandidates.add(new Cube(x, y, z));
                        }
                    }
                }
            }

            Set<Cube> newGeneration = new HashSet<>();
            for (Cube candidate : evaluationCandidates) {
                int neighbours = 0;
                for (int x = candidate.x - 1; x <= candidate.x + 1; x++) {
                    for (int y = candidate.y - 1; y <= candidate.y + 1; y++) {
                        for (int z = candidate.z - 1; z <= candidate.z + 1; z++) {
                            if (x == candidate.x && y == candidate.y && z == candidate.z) {
                                continue;
                            }
                            if (activeCubes.contains(new Cube(x, y, z))) {
                                neighbours++;
                            }
                        }
                    }
                }
                if (activeCubes.contains(candidate)) {
                    if (neighbours == 2 || neighbours == 3) {
                        newGeneration.add(candidate);
                    }
                } else if (neighbours == 3) {
                    newGeneration.add(candidate);
                }
            }
            activeCubes = newGeneration;
            generation++;
        }

        return activeCubes.size();
    }

    @Override
    protected Object part2() {
        Set<HyperCube> activeHyperCubes = parseInputPart2();
        int generation = 1;
        while (generation <= 6) {
            Set<HyperCube> evaluationCandidates = new HashSet<>();
            for (HyperCube cube : activeHyperCubes) {
                for (int x = cube.x - 1; x <= cube.x + 1; x++) {
                    for (int y = cube.y - 1; y <= cube.y + 1; y++) {
                        for (int z = cube.z - 1; z <= cube.z + 1; z++) {
                            for (int w = cube.w - 1; w <= cube.w + 1; w++) {
                                evaluationCandidates.add(new HyperCube(x, y, z, w));
                            }
                        }
                    }
                }
            }

            Set<HyperCube> newGeneration = new HashSet<>();
            for (HyperCube candidate : evaluationCandidates) {
                int neighbours = 0;
                for (int x = candidate.x - 1; x <= candidate.x + 1; x++) {
                    for (int y = candidate.y - 1; y <= candidate.y + 1; y++) {
                        for (int z = candidate.z - 1; z <= candidate.z + 1; z++) {
                            for (int w = candidate.w - 1; w <= candidate.w + 1; w++) {
                                if (x == candidate.x && y == candidate.y && z == candidate.z && w == candidate.w) {
                                    continue;
                                }
                                if (activeHyperCubes.contains(new HyperCube(x, y, z, w))) {
                                    neighbours++;
                                }
                            }
                        }
                    }
                }
                if (activeHyperCubes.contains(candidate)) {
                    if (neighbours == 2 || neighbours == 3) {
                        newGeneration.add(candidate);
                    }
                } else if (neighbours == 3) {
                    newGeneration.add(candidate);
                }
            }
            activeHyperCubes = newGeneration;
            generation++;
        }

        return activeHyperCubes.size();

    }

    private Set<Cube> parseInputPart1() {
        Set<Cube> startingCubes = new HashSet<>();
        for (int y = 0; y < input.size(); y++) {
            String row = input.get(y);
            for (int x = 0; x < row.length(); x++ ) {
                if (row.charAt(x) == '#') {
                    startingCubes.add(new Cube(x, y, 0));
                }
            }
        }
        return startingCubes;
    }

    private Set<HyperCube> parseInputPart2() {
        Set<HyperCube> startingCubes = new HashSet<>();
        for (int y = 0; y < input.size(); y++) {
            String row = input.get(y);
            for (int x = 0; x < row.length(); x++ ) {
                if (row.charAt(x) == '#') {
                    startingCubes.add(new HyperCube(x, y, 0, 0));
                }
            }
        }
        return startingCubes;
    }

    private static class Cube {
        private final int x;
        private final int y;
        private final int z;

        private Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cube cube = (Cube) o;
            return x == cube.x && y == cube.y && z == cube.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    private static class HyperCube {
        private final int x;
        private final int y;
        private final int z;
        private final int w;

        private HyperCube(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HyperCube hyperCube = (HyperCube) o;
            return x == hyperCube.x && y == hyperCube.y && z == hyperCube.z && w == hyperCube.w;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, w);
        }
    }
}

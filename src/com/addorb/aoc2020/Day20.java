package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day20 extends Day {

    private static final int PARSED_TILE_SIZE = 10;

    List<Tile> tiles = new ArrayList<>();
    List<Tile> corners = new ArrayList<>();
    List<Tile> edges = new ArrayList<>();
    List<Tile> normal = new ArrayList<>();

    public Day20() {
        super("input/input-day-20.txt");
    }

    @Override
    protected Object part1() {
        tiles = parseInput();
        categorizeTiles();
        long cornerProduct = 1;
        for (Tile corner : corners) {
            cornerProduct *= corner.id;
        }
        return cornerProduct;
    }

    @Override
    protected Object part2() {
        // Tiles-list should be in normal state, and corners found by part1()
        // Assume tiled image is square for now
        int tileMatrixSize = (int) Math.sqrt(tiles.size());
        Tile[][] tiledImage = new Tile[tileMatrixSize][tileMatrixSize];

        // Lay the puzzle
        for (int y = 0; y < tileMatrixSize; y++) {
            for (int x = 0; x < tileMatrixSize; x++) {
                tiledImage[x][y] = findAndOrientateTile(x, y, tiledImage);
            }
        }

        // Create massive Tile object out of tiled image:
        int croppedTileSize = PARSED_TILE_SIZE - 2;
        int megaTileSize = tileMatrixSize * croppedTileSize;
        boolean[][] megaTileData = new boolean[megaTileSize][megaTileSize];

        // Fill mega tile with data from the tiled image.
        int totalWaveCount = 0;
        for (int x = 0; x < megaTileSize; x++) {
            for (int y = 0; y < megaTileSize; y++) {
                int tileX = x / croppedTileSize;
                int tileY = y / croppedTileSize;
                int xInTile = x % croppedTileSize + 1;
                int yInTile = y % croppedTileSize + 1;
                megaTileData[x][y] = tiledImage[tileX][tileY].get(xInTile, yInTile);
                if (megaTileData[x][y]) {
                    totalWaveCount++;
                }
            }
        }

        Tile megaTile = new Tile(0, megaTileData);

        /*
         *            1111111111
         *  01234567890123456789 x
         * 0                  #
         * 1#    ##    ##    ###
         * 2 #  #  #  #  #  #
         * y
         */
        final int[][] monsterPattern = new int[][]{{18, 0}, {0, 1}, {5, 1}, {6, 1}, {11, 1},
                {12, 1}, {17, 1}, {18, 1}, {19, 1}, {1, 2}, {4, 2}, {7, 2}, {10, 2}, {13, 2},
                {16, 2}};

        int maxX = megaTileSize - 20; // monster pattern area is 20 pixels wide
        int maxY = megaTileSize - 3; // monster pattern area is 3 pixels tall
        int monstersFound = 0;
        for (int flipped = 0; flipped < 2; flipped++) {
            for (int rotation = 0; rotation < 360 && monstersFound == 0; rotation += 90) {
                megaTile.setRotation(rotation);
                for (int x = 0; x < maxX; x++) {
                    for (int y = 0; y < maxY; y++) {
                        if (searchForSeaMonster(x, y, megaTile, monsterPattern)) {
                            monstersFound++;
                        }
                    }
                }
            }
            megaTile.flipX();
        }

        return totalWaveCount - monsterPattern.length * monstersFound;
    }

    private boolean searchForSeaMonster(int x, int y, Tile megaTile, int[][] monsterPattern) {
        for (int[] monsterPixel : monsterPattern) {
            if (!megaTile.get(x + monsterPixel[0], y + monsterPixel[1])) {
                return false;
            }
        }
        return true;
    }

    private Tile findAndOrientateTile(int x, int y, Tile[][] tiledImage) {
        Tile tileToPlace;
        if (x > 0 && x < tiledImage.length - 1 && y > 0 && y < tiledImage[0].length - 1) {
            Tile tileToTheLeft = tiledImage[x - 1][y];
            boolean[] border = tileToTheLeft.getBorder(90);
            tileToPlace = findTileWithBorder(normal, border, 270);
        } else if (x == 0 && y == 0) {
            // top left corner, pick first one and align it according to its outer edges.
            tileToPlace = corners.remove(0);
            tileToPlace.outerEdgeDirections.sort(Integer::compareTo);
            // if outerEdges are 270 + 0, no rotation necessary
            if (tileToPlace.outerEdgeDirections.get(0) == 0 && tileToPlace.outerEdgeDirections
                    .get(1) == 90) {
                tileToPlace.setRotation(270);
            } else if (tileToPlace.outerEdgeDirections.get(0) == 90) {
                tileToPlace.setRotation(180);
            } else if (tileToPlace.outerEdgeDirections.get(0) == 180) {
                tileToPlace.setRotation(90);
            }
        } else if (x > 0 && x < tiledImage.length - 1 && y == 0) {
            // top edge
            Tile tileToTheLeft = tiledImage[x - 1][y];
            boolean[] border = tileToTheLeft.getBorder(90);
            tileToPlace = findTileWithBorder(edges, border, 270);
        } else if (x == tiledImage.length - 1 && y == 0) {
            // top right corner
            Tile tileToTheLeft = tiledImage[x - 1][y];
            boolean[] border = tileToTheLeft.getBorder(90);
            tileToPlace = findTileWithBorder(corners, border, 270);
        } else if (x == 0 && y > 0 && y < tiledImage[0].length - 1) {
            // left edge
            Tile tileAbove = tiledImage[x][y - 1];
            boolean[] border = tileAbove.getBorder(180);
            tileToPlace = findTileWithBorder(edges, border, 0);
        } else if (x == tiledImage.length - 1 && y > 0 && y < tiledImage[0].length - 1) {
            // right edge
            Tile tileAbove = tiledImage[x][y - 1];
            boolean[] border = tileAbove.getBorder(180);
            tileToPlace = findTileWithBorder(edges, border, 0);
        } else if (x == 0 && y == tiledImage[0].length - 1) {
            // bottom left corner
            Tile tileAbove = tiledImage[x][y - 1];
            boolean[] border = tileAbove.getBorder(180);
            tileToPlace = findTileWithBorder(corners, border, 0);
        } else if (x > 0 && x < tiledImage.length - 1 && y == tiledImage[0].length - 1) {
            // bottom edge
            Tile tileAbove = tiledImage[x][y - 1];
            boolean[] border = tileAbove.getBorder(180);
            tileToPlace = findTileWithBorder(edges, border, 0);
        } else {
            // bottom right corner
            Tile tileAbove = tiledImage[x][y - 1];
            boolean[] border = tileAbove.getBorder(180);
            tileToPlace = findTileWithBorder(corners, border, 0);
        }
        return tileToPlace;
    }

    private Tile findTileWithBorder(List<Tile> candidates, boolean[] borderToAlign,
            int directionToAlign) {
        Tile pickedCandidate = null;
        for (Tile candidate : candidates) {
            for (int rotation = 0; rotation < 360; rotation += 90) {
                candidate.setRotation(rotation);
                boolean[] candidateBorder = candidate.getBorder(directionToAlign);
                if (Arrays.equals(borderToAlign, candidateBorder)) {
                    pickedCandidate = candidate;
                    break;
                }
            }
            if (pickedCandidate != null) {
                break;
            }
            candidate.flipX();
            for (int rotation = 0; rotation < 360; rotation += 90) {
                candidate.setRotation(rotation);
                boolean[] candidateBorder = candidate.getBorder(directionToAlign);
                if (Arrays.equals(borderToAlign, candidateBorder)) {
                    pickedCandidate = candidate;
                    break;
                }
            }
        }

        candidates.remove(pickedCandidate);
        return pickedCandidate;
    }

    private void categorizeTiles() {
        for (Tile tile : tiles) {
            int edgeMatch = 0;
            for (int direction = 0; direction < 360; direction += 90) {
                boolean[] edge = tile.getBorder(direction);
                tile.setNormal();
                int preEdgeMatch = edgeMatch;
                for (Tile otherTile : tiles) {
                    if (tile == otherTile) {
                        continue;
                    }
                    for (int otherDirection = 0; otherDirection < 360; otherDirection += 90) {
                        boolean[] otherEdge = otherTile.getBorder(otherDirection);
                        otherTile.setRotation(180);
                        boolean[] otherFlippedEdge = otherTile.getBorder(otherDirection);
                        otherTile.setNormal();
                        if (Arrays.equals(edge, otherEdge)) {
                            edgeMatch++;
                        } else if (Arrays.equals(edge, otherFlippedEdge)) {
                            edgeMatch++;
                        }
                    }
                }
                if (edgeMatch == preEdgeMatch) {
                    // This edge has no matches among all the tiles, this will be of use later
                    // when we orientate the pieces.
                    tile.outerEdgeDirections.add(direction);
                }
            }
            if (edgeMatch == 2) {
                corners.add(tile);
            } else if (edgeMatch == 3) {
                edges.add(tile);
            } else if (edgeMatch == 4) {
                normal.add(tile);
            } else {
                throw new IllegalStateException("A tile had more than 4 or less than 2 neighbours");
            }
        }
    }

    private List<Tile> parseInput() {
        List<Tile> parsedTiles = new ArrayList<>();
        int parsePhase = 0; // 0 = metadata, 1 = imagedata
        boolean[][] data = null;
        long id = 0;
        int y = 0;
        for (String row : input) {
            if (parsePhase == 0) {
                String[] tokens = row.split(" ");
                id = Long.parseLong(tokens[1].substring(0, tokens[1].length() - 1));
                parsePhase = 1;
                data = new boolean[PARSED_TILE_SIZE][PARSED_TILE_SIZE];
            } else {
                if (row.isEmpty()) {
                    parsedTiles.add(new Tile(id, data));
                    y = 0;
                    parsePhase = 0;
                } else {
                    for (int x = 0; x < PARSED_TILE_SIZE; x++) {
                        data[x][y] = row.charAt(x) == '#';
                    }
                    y++;
                }
            }
        }
        return parsedTiles;
    }


    private static class Tile {
        private final int sizeX;
        private final int sizeY;
        private final int inverseX;
        private final int inverseY;
        private final long id;
        private final boolean[][] imageData;
        private final List<Integer> outerEdgeDirections = new ArrayList<>();
        private int rotation = 0;
        private boolean flippedX = false;
        private boolean flippedY = false;

        private Tile(long id, boolean[][] imageData) {
            this.id = id;
            this.imageData = imageData;
            sizeX = imageData.length;
            sizeY = imageData[0].length;
            inverseX = sizeX - 1;
            inverseY = sizeY - 1;
        }

        private void setRotation(int rotation) {
            this.rotation = rotation;
        }

        private void flipX() {
            flippedX = !flippedX;
        }

        @SuppressWarnings("unused")
        private void flipY() {
            flippedY = !flippedY;
        }

        private void setNormal() {
            rotation = 0;
            flippedX = false;
            flippedY = false;
        }

        @SuppressWarnings("SuspiciousNameCombination")
        private boolean get(int x, int y) {
            if (flippedX) {
                x = inverseX - x;
            }
            if (flippedY) {
                y = inverseY - y;
            }
            if (rotation == 90) {
                int newY = inverseX - x;
                x = y;
                y = newY;
            } else if (rotation == 180) {
                y = inverseY - y;
                x = inverseX - x;
            } else if (rotation == 270) {
                int newX = inverseY - y;
                y = x;
                x = newX;
            }
            return imageData[x][y];
        }

        /*
         *      0
         *    +----+
         *    |    |
         * 270|    |90
         *    |    |
         *    +----+
         *     180
         */
        private boolean[] getBorder(int direction) {
            boolean[] edge;
            if (direction == 0 || direction == 180) {
                edge = new boolean[sizeX];
                int y = direction == 0 ? 0 : sizeY - 1;
                for (int x = 0; x < sizeX; x++) {
                    edge[x] = get(x, y);
                }
            } else {
                edge = new boolean[sizeY];
                int x = direction == 90 ? sizeX - 1 : 0;
                for (int y = 0; y < sizeY; y++) {
                    edge[y] = get(x, y);
                }
            }
            return edge;
        }
    }
}

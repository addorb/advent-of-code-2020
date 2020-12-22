package com.addorb.aoc2020;

import java.util.*;

public class Day22 extends Day {

    public Day22() {
        super("input/input-day-22.txt");
    }

    @Override
    protected Object part1() {
        List<Integer> player1 = new ArrayList<>();
        List<Integer> player2 = new ArrayList<>();
        parseInput(player1, player2);
        return calculateScore(playCombat(player1, player2));
    }

    @Override
    protected Object part2() {
        List<Integer> player1 = new ArrayList<>();
        List<Integer> player2 = new ArrayList<>();
        parseInput(player1, player2);
        int winner = playRecursiveCombat(player1, player2);
        List<Integer> winningDeck = winner == 1 ? player1 : player2;
        return calculateScore(winningDeck);
    }

    private List<Integer> playCombat(List<Integer> player1, List<Integer> player2) {
        while (!player1.isEmpty() && !player2.isEmpty()) {
            int player1Card = player1.remove(0);
            int player2Card = player2.remove(0);
            if (player1Card > player2Card) {
                player1.add(player1Card);
                player1.add(player2Card);
            } else if (player2Card > player1Card) {
                player2.add(player2Card);
                player2.add(player1Card);
            }
        }
        return player1.isEmpty() ? player2 : player1;
    }

    private int playRecursiveCombat(List<Integer> player1, List<Integer> player2) {
        Set<GameData> history = new HashSet<>();
        while (!player1.isEmpty() && !player2.isEmpty()) {
            GameData round = new GameData(new ArrayList<>(player1), new ArrayList<>(player2));
            if (history.contains(round)) {
                return 1;
            }
            history.add(round);
            int player1Card = player1.remove(0);
            int player2Card = player2.remove(0);
            if (player1.size() >= player1Card && player2.size() >= player2Card) {
                List<Integer> player1SubGameDeck = new ArrayList<>(player1.subList(0, player1Card));
                List<Integer> player2SubGameDeck = new ArrayList<>(player2.subList(0, player2Card));
                if (playRecursiveCombat(player1SubGameDeck, player2SubGameDeck) == 1) {
                    player1.add(player1Card);
                    player1.add(player2Card);
                } else {
                    player2.add(player2Card);
                    player2.add(player1Card);
                }
            } else if (player1Card > player2Card) {
                player1.add(player1Card);
                player1.add(player2Card);
            } else if (player2Card > player1Card) {
                player2.add(player2Card);
                player2.add(player1Card);
            }
        }
        return player1.isEmpty() ? 2 : 1;
    }

    private long calculateScore(List<Integer> winningDeck) {
        int sum = 0;
        final int deckSize = winningDeck.size();
        for (int i = 0; i < deckSize; i++) {
            sum += (deckSize - i) * winningDeck.get(i);
        }
        return sum;
    }

    private void parseInput(List<Integer> player1, List<Integer> player2) {
        int player = 0;
        for (String row : input) {
            if (row.equals("Player 1:")) {
                player = 1;
            } else if (row.equals("Player 2:")) {
                player = 2;
            } else if (!row.isEmpty()) {
                if (player == 1) {
                    player1.add(Integer.parseInt(row));
                } else {
                    player2.add(Integer.parseInt(row));
                }
            }
        }
    }

    private static class GameData {
        List<Integer> p1StartingDeck;
        List<Integer> p2StartingDeck;

        private GameData(List<Integer> p1StartingDeck, List<Integer> p2StartingDeck) {
            this.p1StartingDeck = p1StartingDeck;
            this.p2StartingDeck = p2StartingDeck;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            GameData other = (GameData) o;
            if (p1StartingDeck.size() != other.p1StartingDeck.size()) {
                return false;
            } else if (p2StartingDeck.size() != other.p2StartingDeck.size()) {
                return false;
            } else {
                for (int i = 0; i < p1StartingDeck.size(); i++) {
                    if (!p1StartingDeck.get(i).equals(other.p1StartingDeck.get(i))) {
                        return false;
                    }
                }
                for (int i = 0; i < p2StartingDeck.size(); i++) {
                    if (!p2StartingDeck.get(i).equals(other.p2StartingDeck.get(i))) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(p1StartingDeck, p2StartingDeck);
        }
    }
}

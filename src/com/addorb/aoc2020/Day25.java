package com.addorb.aoc2020;

public class Day25 extends Day {

    public Day25() {
        super("input/input-day-25.txt");
    }

    @Override
    protected Object part1() {
        final long cardPubKey = Integer.parseInt(input.get(0));
        final long doorPubKey = Integer.parseInt(input.get(1));
        final long cardSecretLoopSize = findSecretLoopSize(7L, cardPubKey);
        return transformSubjectNumber(doorPubKey, cardSecretLoopSize);
    }

    private int findSecretLoopSize(long subjectNbr, long publicKey) {
        int loops = 0;
        long value = 1;
        while (value != publicKey) {
            value = value * subjectNbr;
            value = value % 20201227;
            loops++;
        }
        return loops;
    }

    private long transformSubjectNumber(long subjectNbr, long loops) {
        long value = 1;
        for (int i = 0; i < loops; i++) {
            value = value * subjectNbr;
            value = value % 20201227;
        }
        return value;
    }

    @Override
    protected Object part2() {
        return "Merry Christmas!";
    }
}

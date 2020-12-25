package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.List;

public class Day13 extends Day {

    private final long currentTime;
    private final List<Bus> buses;

    public Day13() {
        super("input/input-day-13.txt");
        currentTime = parseStartingTime();
        buses = parseBuses();
    }

    @Override
    protected Object part1() {
        long bestBus = -1;
        long bestWaitTime = Integer.MAX_VALUE;
        for (Bus bus : buses) {
            // unlikely, but if it happens, we will know.
            if (currentTime % bus.id == 0) {
                bestBus = bus.id;
                bestWaitTime = 0;
                break;
            }
            long waitTime = (currentTime / bus.id + 1) * bus.id - currentTime;
            if (waitTime < bestWaitTime) {
                bestBus = bus.id;
                bestWaitTime = waitTime;
            }
        }

        return bestBus * bestWaitTime;
    }


    @Override
    protected Object part2() {
        boolean foundSequence = false;
        long startFrom = 0;
        long firstOccurrence = findNextLineup(buses.get(1), startFrom, buses.get(0).id, 0);
        long secondOccurrence = findNextLineup(buses.get(1), firstOccurrence, buses.get(0).id, 0);
        startFrom = firstOccurrence;
        long jumpLength = secondOccurrence - firstOccurrence;
        int currentBusIndex = 1;
        long tAtStartOfSequence = 0L;
        long accOffset = 0;
        while (!foundSequence) {
            Bus busToLineup = buses.get(currentBusIndex);
            firstOccurrence = findNextLineup(busToLineup, startFrom, jumpLength, accOffset);
            secondOccurrence = findNextLineup(busToLineup, firstOccurrence, jumpLength,
                    accOffset);
            jumpLength = secondOccurrence - firstOccurrence;
            currentBusIndex++;
            if (currentBusIndex >= buses.size()) {
                foundSequence = true;
                tAtStartOfSequence = firstOccurrence;
            } else {
                accOffset += busToLineup.offset;
                startFrom = firstOccurrence;
            }
        }
        return tAtStartOfSequence;
    }


    /**
     * Find the next point at which one bus lines up in sequence with all previous buses
     *
     * @param bus2       the "other" bus
     * @param startFrom  where in the timetable to start
     * @param jumpLength how long jumps to do for the first bus. This should be evenly divisible by
     *                   the first bus id.
     * @param accOffset  Accumulated offset from the first bus, to the bus prior to bus2
     * @return how long from the start (0) we found the lineup. This should be evenly divisible by
     * the first bus id .
     */
    private long findNextLineup(Bus bus2, long startFrom, long jumpLength, long accOffset) {
        final long offset = accOffset + bus2.offset;
        long bus1acc = startFrom;
        long bus2acc;

        long minStartBus2 = (bus1acc + offset) / bus2.id * bus2.id;
        if (minStartBus2 > bus1acc + offset) {
            bus2acc = minStartBus2;
        } else {
            bus2acc = minStartBus2 + bus2.id;
        }
        while (bus1acc + offset != bus2acc) {
            if (bus1acc + offset < bus2acc) {
                bus1acc += jumpLength;

                // Since the jumpLength can be really large, let's help bus2 get closer to the next
                // lineup candidate.
                minStartBus2 = (bus1acc + offset) / bus2.id * bus2.id;
                if (minStartBus2 < bus1acc + offset) {
                    bus2acc = minStartBus2;
                } else {
                    bus2acc = minStartBus2 - bus2.id;
                }
            } else if (bus1acc + offset > bus2acc) {
                bus2acc += bus2.id;
            }
        }

        return bus1acc;
    }

    private long parseStartingTime() {
        return Long.parseLong(input.get(0));
    }

    private List<Bus> parseBuses() {
        List<Bus> parsed = new ArrayList<>();
        int offset = 1;
        for (String bus : input.get(1).split(",")) {
            if (bus.charAt(0) == 'x') {
                offset++;
                continue;
            }
            parsed.add(new Bus(Integer.parseInt(bus), offset));
            offset = 1;
        }
        return parsed;
    }

    private static final class Bus {
        private final long id;
        private final long offset;

        private Bus(int id, int offset) {
            this.id = id;
            this.offset = offset;
        }
    }
}

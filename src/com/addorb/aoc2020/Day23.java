package com.addorb.aoc2020;

import java.util.*;

public class Day23 extends Day {

    public Day23() {
        super("input/input-day-23.txt");
    }

    @Override
    protected Object part1() {
        Ring<Integer> ring = parseInput();
        moveCups(ring, 100, 9);
        ring.setCurrentTo(1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(ring.remove());
        }
        return sb.toString();
    }

    @Override
    protected Object part2() {
        final int maxLabel = 1000000;
        Ring<Integer> ring = parseInput();
        ring.prev();
        for (int i = 10; i <= maxLabel; i++) {
            ring.add(i);
            ring.next();
        }
        ring.next();
        moveCups(ring, 10000000, 1000000);
        ring.setCurrentTo(1);
        long first = ring.remove();
        long second = ring.remove();
        return first * second;
    }

    private void moveCups(Ring<Integer> ring, int moves, int maxLabel) {
        List<Integer> threeCups = new ArrayList<>();
        for (int move = 1; move <= moves; move++) {
            threeCups.add(ring.remove());
            threeCups.add(ring.remove());
            threeCups.add(ring.remove());
            Integer oldCurrentCup = ring.getCurrent();
            int destinationCup = oldCurrentCup - 1;
            while (threeCups.size() > 0) {
                if (ring.setCurrentTo(destinationCup)) {
                    ring.add(threeCups.remove(2));
                    ring.add(threeCups.remove(1));
                    ring.add(threeCups.remove(0));
                } else {
                    destinationCup--;
                    if (destinationCup <= 0) {
                        destinationCup = maxLabel;
                    }
                }
            }
            ring.setCurrentTo(oldCurrentCup);
            ring.next();
        }
    }

    private Ring<Integer> parseInput() {
        String cups = input.get(0);
        Ring<Integer> ring = new Ring<>();
        for (int i = 0; i < cups.length(); i++) {
            ring.add(Character.getNumericValue(cups.charAt(i)));
            ring.next();
        }
        // Set current to the first item added.
        ring.next();
        return ring;
    }

    private static class Ring<E> {

        Item<E> current = null;

        Map<E, Item<E>> cache = new HashMap<>();

        public void next() {
            current = current.next;
        }

        public void prev() {
            current = current.prev;
        }

        public E getCurrent() {
            return current.content;
        }

        public boolean setCurrentTo(E target) {
            Item<E> cached = cache.get(target);
            if (cached != null) {
                current = cached;
            }
            return cached != null;
        }

        /**
         * Add item after current item
         *
         * @param item the item
         */
        public void add(E item) {
            if (current == null) {
                current = new Item<>(item);
                current.next = current;
                current.prev = current;
                cache.put(item, current);
            } else {
                Item<E> newItem = new Item<>(item);
                Item<E> oldNext = current.next;
                current.next = newItem;
                newItem.prev = current;
                newItem.next = oldNext;
                oldNext.prev = newItem;
                cache.put(item, newItem);
            }
        }

        /**
         * Removes item after current item.
         *
         * @return the item
         */
        public E remove() {
            if (current == null) {
                throw new IllegalStateException("try to remove item from an empty ring");
            } else {
                Item<E> toRemove = current.next;
                if (toRemove == current) {
                    current = null;
                } else {
                    current.next = toRemove.next;
                    toRemove.next.prev = current;
                }
                cache.remove(toRemove.content);
                return toRemove.content;
            }
        }

        private static class Item<E> {

            private Item<E> next;
            private Item<E> prev;
            private final E content;

            private Item(E content) {
                this.content = content;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                Item<?> item = (Item<?>) o;
                return Objects.equals(content, item.content);
            }

            @Override
            public int hashCode() {
                return Objects.hash(content);
            }
        }
    }
}

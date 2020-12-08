package com.addorb.aoc2020;

import java.util.ArrayList;
import java.util.List;

public final class Day2 extends Day {

    private final List<PasswordData> passwords;

    public Day2() {
        super("input/input-day-2.txt");
        passwords = convertInput();
    }

    @Override
    protected Object part1() {
        int validPasswords = 0;
        for (PasswordData data : passwords) {
            String password = data.password;
            int occurrences = 0;
            for (int i = 0; i < password.length(); i++) {
                if (password.charAt(i) == data.requirement) {
                    occurrences++;
                }
            }
            if (occurrences >= data.rangeStart && occurrences <= data.rangeEnd) {
                validPasswords++;
            }
        }
        return validPasswords;
    }

    @Override
    protected Object part2() {
        int validPasswords = 0;
        for (PasswordData data : passwords) {
            String password = data.password;
            if (password.charAt(data.rangeStart - 1) == data.requirement ^
                    password.charAt(data.rangeEnd - 1) == data.requirement) {
                validPasswords++;
            }
        }
        return validPasswords;
    }

    private List<PasswordData> convertInput() {
        List<PasswordData> passwords = new ArrayList<>(input.size());
        for (String entry : input) {
            entry = entry.trim();
            String[] fields = entry.split(" ");
            String[] range = fields[0].split("-");
            passwords.add(new PasswordData(Integer.parseInt(range[0]), Integer.parseInt(range[1]), fields[1].charAt(0),
                    fields[2]));
        }
        return passwords;
    }

    private static class PasswordData {
        private final int rangeStart;
        private final int rangeEnd;
        private final char requirement;
        private final String password;

        private PasswordData(int rangeStart, int rangeEnd, char requirement, String password) {
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
            this.requirement = requirement;
            this.password = password;
        }
    }
}

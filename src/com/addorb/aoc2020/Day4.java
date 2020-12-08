package com.addorb.aoc2020;

import java.util.*;
import java.util.regex.Pattern;

public class Day4 extends Day {

    private static final String[] REQUIRED_FIELDS = new String[]{"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};
    private static final Pattern COLOR_REGEX = Pattern.compile("^#[0-9,a-f]{6}$");
    private static final String[] VALID_EYE_COLORS = new String[]{"amb", "blu", "brn", "gry", "grn", "hzl", "oth"};
    private static final Pattern PASSPORT_ID_REGEX = Pattern.compile("^[0-9]{9}$");

    private final List<Map<String, String>> passports;

    public Day4() {
        super("input/input-day-4.txt");
        passports = parsePassports();
    }

    @Override
    protected Object part1() {
        return passports.stream().filter(this::isValidEntryBasic).count();
    }

    @Override
    protected Object part2() {
        return passports.stream().filter(this::isValidEntryAdvanced).count();
    }

    private List<Map<String, String>> parsePassports() {
        List<Map<String, String>> parsedList = new ArrayList<>();
        Map<String, String> entry = new HashMap<>();
        for (String row : input) {
            if (row.isEmpty()) {
                parsedList.add(entry);
                entry = new HashMap<>();
                continue;
            }
            String[] kvp = row.split(" ");
            for (String pair : kvp) {
                String[] kvpSplit = pair.split(":");
                entry.put(kvpSplit[0], kvpSplit[1]);
            }
        }
        parsedList.add(entry);
        return parsedList;
    }

    private boolean isValidEntryBasic(Map<String, String> passport) {
        for (String required : REQUIRED_FIELDS) {
            if (!passport.containsKey(required)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidEntryAdvanced(Map<String, String> passport) {
        if (!isValidEntryBasic(passport)) {
            return false;
        }
        int birthYear = Integer.parseInt(passport.get("byr"));
        if (birthYear < 1920 || birthYear > 2002) {
            return false;
        }

        int issueYear = Integer.parseInt(passport.get("iyr"));
        if (issueYear < 2010 || issueYear > 2020) {
            return false;
        }
        int expiryYear = Integer.parseInt(passport.get("eyr"));
        if (expiryYear < 2020 || expiryYear > 2030) {
            return false;
        }
        if (!validateHeight(passport.get("hgt"))) {
            return false;
        }
        if (!COLOR_REGEX.matcher(passport.get("hcl")).matches()) {
            return false;
        }
        String eyeColor = passport.get("ecl");
        if (Arrays.stream(VALID_EYE_COLORS).noneMatch(p -> p.equals(eyeColor))) {
            return false;
        }
        return PASSPORT_ID_REGEX.matcher(passport.get("pid")).matches();
    }


    private boolean validateHeight(String height) {
        int cmPos = height.indexOf("cm");
        if (cmPos > 0) {
            String digits = height.substring(0, cmPos);
            try {
                int cm = Integer.parseInt(digits);
                if (cm >= 150 && cm <= 193) {
                    return true;
                }
            } catch (NumberFormatException e) {
                System.out.println("could not parse height : " + height);
                return false;
            }
        }
        int inPos = height.indexOf("in");
        if (inPos > 0) {
            String digits = height.substring(0, inPos);
            try {
                int in = Integer.parseInt(digits);
                if (in >= 59 && in <= 76) {
                    return true;
                }
            } catch (NumberFormatException e) {
                System.out.println("could not parse height : " + height);
                return false;
            }
        }
        return false;
    }
}

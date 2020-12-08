package com.addorb.aoc2020;

public class Day8 extends Day {

    final Line[] code;

    public Day8() {
        super("input/input-day-8.txt");
        code = parseInput();
    }

    @Override
    protected Object part1() {
        int pc = 0;
        int acc = 0;
        while (!code[pc].visited) {
            code[pc].visited = true;
            switch (code[pc].instr) {
                case "nop" : {
                    pc++;
                    break;
                }
                case "acc" : {
                    acc += code[pc].arg;
                    pc++;
                    break;
                }
                case "jmp" :{
                    pc += code[pc].arg;
                    break;
                }
            }
        }
        return acc;
    }

    @Override
    protected Object part2() {
        resetVisited();
        int correctResult = 0;
        for (int i = 0; i < code.length; i++) {
            swapInstr(i);
            try {
                correctResult = testProgram();
                break;
            } catch (RuntimeException e) {
                swapInstr(i);
                resetVisited();
            }
        }

        return correctResult;
    }

    private int testProgram() throws RuntimeException {
        int pc = 0;
        int acc = 0;
        while (pc != code.length && !code[pc].visited) {
            code[pc].visited = true;
            switch (code[pc].instr) {
                case "nop" -> pc++;
                case "acc" -> {
                    acc += code[pc].arg;
                    pc++;
                }
                case "jmp" -> pc += code[pc].arg;
            }
        }
        if (pc == code.length) {
            return acc;
        } else {
            throw new RuntimeException("Code looped");
        }
    }

    private void swapInstr(int i) {
        if (code[i].instr.equals("nop")) {
            code[i].instr = "jmp";
        } else if (code[i].instr.equals("jmp")) {
            code[i].instr = "nop";
        }
    }

    private void resetVisited() {
        for (Line line : code) {
            line.visited = false;
        }
    }

    private Line[] parseInput () {
        int count = input.size();
        Line[] code = new Line[count];
        for (int i = 0; i < count; i++) {
            String[] tokens = input.get(i).split(" ");
            code[i] = new Line(tokens[0], Integer.parseInt(tokens[1]));
        }
        return code;
    }

     private static class Line {
        private String instr;
        private final int arg;
        private boolean visited;

        Line(String instr, int arg) {
            this.instr = instr;
            this.arg = arg;
            visited = false;
        }
    }
}

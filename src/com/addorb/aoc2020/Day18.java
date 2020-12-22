package com.addorb.aoc2020;

import java.util.*;

public class Day18 extends Day {

    public Day18() {
        super("input/input-day-18.txt");
    }

    @Override
    protected Object part1() {
        return processExpressions(input, this::part1OperatorPrecedence);
    }

    @Override
    protected Object part2() {
        return processExpressions(input, this::part2OperatorPrecedence);
    }

    private long processExpressions(List<String> expressions,
            OperatorPrecedence operatorPrecedence) {
        long sum = 0;
        for (String expression : expressions) {
            sum += processExpression(expression, operatorPrecedence);
        }
        return sum;
    }

    private long processExpression(String expression, OperatorPrecedence operatorPrecedence) {
        final int length = expression.length();
        final Stack<Character> operators = new Stack<>();
        final List<Character> RPNtokens = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            char token = expression.charAt(i);
            if (token == '*' || token == '+') {
                while (operators.size() > 0 &&
                        (operators.peek() == '*' || operators.peek() == '+') &&
                        operatorPrecedence.compare(operators.peek(), token) == 1 &&
                        operators.peek() != '(') {
                    char operator = operators.pop();
                    RPNtokens.add(operator);
                }
                operators.push(token);
            } else if (token == '(') {
                operators.push(token);
            } else if (token == ')') {
                while (operators.peek() != '(') {
                    char operator = operators.pop();
                    RPNtokens.add(operator);
                }
                if (operators.peek() == '(') {
                    operators.pop();
                }
            } else if (token != ' ') {
                RPNtokens.add(token);
            }
        }
        while (!operators.isEmpty()) {
            RPNtokens.add(operators.pop());
        }

        Stack<Long> stack = new Stack<>();
        for (Character token : RPNtokens) {
            if (token == '*') {
                stack.push(stack.pop() * stack.pop());
            } else if (token == '+') {
                stack.push(stack.pop() + stack.pop());
            } else {
                stack.push((long) Character.getNumericValue(token));
            }
        }
        return stack.pop();
    }

    private int part2OperatorPrecedence(char operator1, char operator2) {
        if (operator1 == '*' && operator2 == '+') {
            return -1;
        } else if (operator2 == '*' && operator1 == '+') {
            return 1;
        } else {
            return 0;
        }
    }

    private int part1OperatorPrecedence(char operator1, char operator2) {
        return 1;
    }

    private interface OperatorPrecedence {
        int compare(char operator1, char operator2);
    }
}

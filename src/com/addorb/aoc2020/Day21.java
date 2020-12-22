package com.addorb.aoc2020;

import java.util.*;

public class Day21 extends Day {

    final private List<Food> foods;

    public Day21() {
        super("input/input-day-21.txt");
        foods = parseInput();
    }

    @Override
    protected Object part1() {
        final Map<String, Set<String>> ingredientAllergens = findIngredientAllergens();
        final Set<String> allIngredients = buildIngredientsSet();
        final Set<String> nonAllergenIngredients =
                findNonAllergenIngredients(ingredientAllergens, allIngredients);
        int usageSum = 0;
        for (String nonAllergenIngredient : nonAllergenIngredients) {
            for (Food food : foods) {
                for (String ingredientInFood : food.ingredients) {
                    if (nonAllergenIngredient.equals(ingredientInFood)) {
                        usageSum++;
                    }
                }
            }
        }
        return usageSum;
    }

    @Override
    protected Object part2() {
        final Map<String, Set<String>> allergensToIngredient = findIngredientAllergens();
        Map<String, String> dangerousIngredients = new TreeMap<>();

        while(!oneToOneMapped(allergensToIngredient)) {
            for (Map.Entry<String, Set<String>> entry : allergensToIngredient.entrySet()) {
                if (entry.getValue().size() == 1) {
                    String ingredient = entry.getValue().iterator().next();
                    dangerousIngredients.put(entry.getKey(), entry.getValue().iterator().next());
                    for (Map.Entry<String, Set<String>> entryInner: allergensToIngredient.entrySet()) {
                        if (entry == entryInner) {
                            continue;
                        }
                        entryInner.getValue().remove(ingredient);
                    }
                }
            }
        }
        return String.join(",", dangerousIngredients.values());
    }

    private boolean oneToOneMapped( Map<String, Set<String>> ingredientAllergens) {
        for (Map.Entry<String, Set<String>> entry : ingredientAllergens.entrySet()) {
            if (entry.getValue().size() > 1) {
                return false;
            }
        }
        return true;
    }

    private Set<String> findNonAllergenIngredients(Map<String, Set<String>> ingredientAllergens,
            Set<String> allIngredients) {
        for (Map.Entry<String, Set<String>> entry : ingredientAllergens.entrySet()) {
            allIngredients.removeAll(entry.getValue());
        }
        return allIngredients;
    }

    private Map<String, Set<String>> findIngredientAllergens() {
        // k = allergen, v Set of ingredients
        Map<String, Set<String>> allergens = new TreeMap<>();
        for (Food food : foods) {
            for (String allergen : food.allergens) {
                Set<String> ingredients = allergens.computeIfAbsent(allergen,
                        k -> new TreeSet<>(food.ingredients));
                ingredients.retainAll(food.ingredients);
            }
        }
        return allergens;
    }

    private Set<String> buildIngredientsSet() {
        Set<String> superSet = new HashSet<>();
        for (Food food : foods) {
            superSet.addAll(food.ingredients);
        }
        return superSet;
    }

    private List<Food> parseInput() {
        List<Food> foodsFromInput = new ArrayList<>();
        for (String row : input) {
            int startParenthesis = row.indexOf('(');
            int endParenthesis = row.indexOf(')');
            String[] ingredients = row.substring(0, startParenthesis).trim().split(" ");
            String allergensText = row.substring(startParenthesis + 1, endParenthesis);
            String[] allergens = allergensText.substring("contains ".length()).split(", ");
            foodsFromInput.add(new Food(Arrays.asList(ingredients), Arrays.asList(allergens)));
        }
        return foodsFromInput;
    }

    private static class Food {
        final List<String> ingredients;
        final List<String> allergens;

        private Food(List<String> ingredients, List<String> allergens) {
            this.ingredients = ingredients;
            this.allergens = allergens;
        }
    }

}

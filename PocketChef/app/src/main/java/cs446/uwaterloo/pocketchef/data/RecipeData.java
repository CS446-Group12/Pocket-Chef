package cs446.uwaterloo.pocketchef.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cs446.uwaterloo.pocketchef.adapters.RecipeAdapter;
import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class RecipeData {
    private static String TAG = "RecipeData";

    private static boolean ingredientRecipesShown = false;
    private static RecipeAdapter adapter;
    private static List<Recipe> recipesForCurrentIngredients;
    private static HashMap<Recipe, HashSet<Ingredient>> recipesWithMissingIngredients;
    private static List<Recipe> currentRecipes;
    private static HashSet<Recipe> allRecipes;

    public static final RecipeData manager = new RecipeData(Recipe.getDemoRecipes());

    private RecipeData(List<Recipe> recipes) {
        adapter = null;
        recipesForCurrentIngredients = new ArrayList<>();
        recipesWithMissingIngredients = new HashMap<>();
        currentRecipes = new ArrayList<>(recipes);
        allRecipes = new HashSet<>(recipes);
    }

    public static void setAdapter(RecipeAdapter newAdapter) {
        adapter = newAdapter;
    }

    public static List<Recipe> getCurrentRecipes() {
        return currentRecipes;
    }

    protected static HashSet<Recipe> getAllRecipes() {
        return allRecipes;
    }

    protected static List<Recipe> getRecipesForCurrentIngredients() {
        return recipesForCurrentIngredients;
    }

    protected static void setRecipesForCurrentIngredients(List<Recipe> recipes) {
        recipesForCurrentIngredients = recipes;
        if (ingredientRecipesShown) {
            currentRecipes = recipes;
            adapter.notifyDataSetChanged();
        }
    }

    protected static HashMap<Recipe, HashSet<Ingredient>> getRecipesWithMissingIngredients() {
        return recipesWithMissingIngredients;
    }

    protected static void setRecipesWithMissingIngredients(
            HashMap<Recipe, HashSet<Ingredient>> map
    ) {
        recipesWithMissingIngredients = map;
    }

    public static void switchShownRecipes() {
        if (ingredientRecipesShown)
            resetRecipes();
        else
            showRecipesForCurrentIngredients();
    }

    public static void resetRecipes() {
        Log.i(TAG, "Resetting currentRecipes");
        if (ingredientRecipesShown) {
            currentRecipes = new ArrayList<>(allRecipes);
            adapter.notifyDataSetChanged();
        } else {
            HashSet<Recipe> diff = new HashSet<>(allRecipes);
            diff.removeAll(currentRecipes);
            int index = currentRecipes.size();
            currentRecipes.addAll(diff);
            adapter.notifyItemRangeInserted(index, diff.size());
        }
        ingredientRecipesShown = false;
    }

    public static void showRecipesForCurrentIngredients() {
        currentRecipes = recipesForCurrentIngredients;
        ingredientRecipesShown = true;
        adapter.notifyDataSetChanged();
    }

    public static void hideRecipes(Recipe... recipes) {
        hideRecipes(Arrays.asList(recipes));
    }

    public static void hideRecipes(List<Recipe> recipes) {
        recipes.retainAll(allRecipes);
        int index;
        for (final Recipe recipe : recipes) {
            if ((index = currentRecipes.indexOf(recipe)) != -1) {
                Log.i(TAG, "Hiding ingredient: " + recipe.getName());
                currentRecipes.remove(index);
                adapter.notifyItemRemoved(index);
            }
        }
    }

    public static void revealRecipes(Recipe... recipes) {
        revealRecipes(Arrays.asList(recipes));
    }

    public static void revealRecipes(List<Recipe> recipes) {
        recipes.retainAll(allRecipes);
        int index = 0;
        while (index < recipes.size()) {
            if (currentRecipes.contains(recipes.get(index)))
                recipes.remove(index);
            else
                index ++;
        }

        Log.i(TAG, "Revealing recipes: " + Arrays.toString(recipes.toArray()));
        index = currentRecipes.size();
        currentRecipes.addAll(recipes);
        adapter.notifyItemRangeInserted(index, recipes.size());
    }

    public static void removeRecipes(Recipe... recipes) {
        removeRecipes(Arrays.asList(recipes));
    }

    public static void removeRecipes(List<Recipe> recipes) {
        hideRecipes(recipes);
        Log.i(TAG, "Removing recipes: " + Arrays.toString(recipes.toArray()));
        allRecipes.removeAll(recipes);
    }

    public static void addRecipe(Recipe... recipes) {
        addRecipe(Arrays.asList(recipes));
    }

    public static void addRecipe(List<Recipe> recipes) {
        Log.i(TAG, "Adding recipes: " + Arrays.toString(recipes.toArray()));
        allRecipes.addAll(recipes);
        if (!ingredientRecipesShown)
            revealRecipes(recipes);

        new AsyncAddRecipe().execute(recipes.toArray(new Recipe[0]));
    }
}

package cs446.uwaterloo.pocketchef.data;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class AsyncAddRecipe extends AsyncTask<Recipe, Integer, List<Recipe>> {
    private final String TAG = "AsyncAddRecipe";

    private static HashMap<Recipe, HashSet<Ingredient>> missingRecipes;
    private static List<Recipe> completeRecipes;
    private static HashSet<Ingredient> allIngredients;

    public AsyncAddRecipe() {
        super();
    }

    @Override
    protected void onPreExecute() {
        missingRecipes = new HashMap<>(RecipeData.getRecipesWithMissingIngredients());
        completeRecipes = new ArrayList<>(RecipeData.getRecipesForCurrentIngredients());
        allIngredients = new HashSet<>(IngredientData.getAllIngredients());
    }

    @Override
    protected List<Recipe> doInBackground(Recipe... recipes) {
        HashSet<Ingredient> ingredientsUsed;
        for (final Recipe recipe : recipes) {
            ingredientsUsed = new HashSet<>(recipe.getAllIngredients());
            ingredientsUsed.removeAll(allIngredients);
            if (ingredientsUsed.isEmpty())
                completeRecipes.add(recipe);
            else
                missingRecipes.put(recipe, ingredientsUsed);
        }

        return completeRecipes;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        Log.i(TAG, "Matched recipes: " + Arrays.toString(recipes.toArray()));
        RecipeData.setRecipesForCurrentIngredients(recipes);
        Log.i(TAG, "Missing recipes: " + Arrays.toString(missingRecipes.keySet().toArray()));
        RecipeData.setRecipesWithMissingIngredients(missingRecipes);
    }
}

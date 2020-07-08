package cs446.uwaterloo.pocketchef.data;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class AsyncRecipeMap extends AsyncTask<Ingredient, Integer, List<Recipe>> {
    public enum Action { ADD, REMOVE, INITIALIZE }

    private final String TAG = "AsyncRecipeMap";

    private static Action action;
    private static HashMap<Recipe, HashSet<Ingredient>> missingRecipes;
    private static List<Recipe> completeRecipes;

    public AsyncRecipeMap(Action desiredAction) {
        super();
        action = desiredAction;
    }

    @Override
    protected void onPreExecute() {
        missingRecipes = new HashMap<>(RecipeData.getRecipesWithMissingIngredients());
        completeRecipes = new ArrayList<>(RecipeData.getRecipesForCurrentIngredients());
    }

    @Override
    protected List<Recipe> doInBackground(Ingredient... ingredientArray) {
        final List<Ingredient> ingredients = Arrays.asList(ingredientArray);

        switch (action) {
        case ADD:
            // If new ingredients have been added, then search through all recipes that currently
            // have missing ingredients. If a recipe was missing one of the ingredients added, then
            // remove that ingredient from the collection of missing ones. If a recipe is no longer
            // missing any ingredients, mark it as such
            Log.i(TAG, "Updating recipes due to addition of ingredients");
            Iterator<Map.Entry<Recipe, HashSet<Ingredient>>> mapIterator
                    = missingRecipes.entrySet().iterator();
            Map.Entry<Recipe, HashSet<Ingredient>> mapEntry;
            while (mapIterator.hasNext()) {
                mapEntry = mapIterator.next();
                if (mapEntry.getValue().removeAll(ingredients) && mapEntry.getValue().isEmpty()) {
                    completeRecipes.add(mapEntry.getKey());
                    mapIterator.remove();
                }
            }
            break;

        case REMOVE:
            // If ingredients are being removed, then there are two scenarios to deal with. Firstly,
            // any complete recipes that require the removed ingredient must now be marked as
            // incomplete. Second, any incomplete recipe which also requires this ingredient must be
            // updated to indicate that it is additionally missing the current ingredient.
            Log.i(TAG, "Updating recipes due to removal of ingredients");
            HashSet<Ingredient> missingIngredients;
            HashSet<Ingredient> ingredientsUsed;

            for (final Map.Entry<Recipe, HashSet<Ingredient>> entry : missingRecipes.entrySet()) {
                missingIngredients = new HashSet<>(ingredients);
                ingredientsUsed = entry.getKey().getAllIngredients();
                missingIngredients.retainAll(ingredientsUsed);
                entry.getValue().addAll(missingIngredients);
            }

            Iterator<Recipe> recipeIterator = completeRecipes.iterator();
            Recipe completeRecipe;
            while (recipeIterator.hasNext()) {
                completeRecipe = recipeIterator.next();
                missingIngredients = new HashSet<>(ingredients);
                ingredientsUsed = completeRecipe.getAllIngredients();
                missingIngredients.retainAll(ingredientsUsed);
                if (!missingIngredients.isEmpty()) {
                    recipeIterator.remove();
                    missingRecipes.put(completeRecipe, missingIngredients);
                }
            }
            break;

        case INITIALIZE:
            // If initializing, then there are currently no complete or incomplete recipes. All
            // recipes must be stepped through and determined, based off only the incoming
            // ingredientsArray, whether they are or are not complete.
            Log.i(TAG, "Initializing recipes for the current ingredients");
            HashSet<Recipe> recipes = RecipeData.getAllRecipes();
            for (final Recipe recipe : recipes) {
                ingredientsUsed = new HashSet<>(recipe.getAllIngredients());
                ingredientsUsed.removeAll(ingredients);
                if (ingredientsUsed.isEmpty()) {
                    completeRecipes.add(recipe);
                } else {
                    missingRecipes.put(recipe, ingredientsUsed);
                }
            }
        }

        return completeRecipes;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        Log.d(TAG, "Matched recipes: " + Arrays.toString(recipes.toArray()));
        RecipeData.setRecipesForCurrentIngredients(recipes);
        Log.d(TAG, "Missing recipes: " + Arrays.toString(missingRecipes.keySet().toArray()));
        RecipeData.setRecipesWithMissingIngredients(missingRecipes);
    }
}

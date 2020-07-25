package cs446.uwaterloo.pocketchef.data;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.DatabaseMetaData;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class AsyncIngredientTask extends AsyncTask<Ingredient, Integer, List<Ingredient>> {
    public enum Action { ADD, REMOVE, INITIALIZE }

    private final String TAG = "AsyncRecipeMap";

    private Action action;

    public AsyncIngredientTask(Action desiredAction) {
        super();
        action = desiredAction;
    }

    @Override
    protected List<Ingredient> doInBackground(Ingredient... ingredientArray) {
        final List<Ingredient> ingredients = Arrays.asList(ingredientArray);
        ConcurrentSkipListSet<Recipe> recipesForCurrent
                = RecipeData.manager.getRecipesForCurrentIngredients();
        ConcurrentSkipListMap<Recipe, ConcurrentSkipListSet<Ingredient>> lackingRecipes
                = RecipeData.manager.getRecipesWithMissingIngredients();

        switch (action) {
            case INITIALIZE:
                // If initializing, then there are currently no complete or incomplete recipes. All
                // recipes must be stepped through and determined, based off only the incoming
                // ingredientsArray, whether they are complete.
                Log.i(TAG, "Initializing recipes for the current ingredients - Start");

                ConcurrentSkipListSet<Ingredient> ingredientsUsed;
                for (final Recipe recipe : RecipeData.manager.getAllRecipes()) {
                    ingredientsUsed = new ConcurrentSkipListSet<>(recipe.getAllIngredients());
                    ingredientsUsed.removeAll(IngredientData.manager.sortedData);
                    if (ingredientsUsed.isEmpty())
                        recipesForCurrent.add(recipe);
                    else
                        lackingRecipes.put(recipe, ingredientsUsed);
                }

                RecipeData.manager.markChangeInRecipesForCurrentIngredients();
                Log.i(TAG, "Initializing recipes for the current ingredients - End");
                break;

            case ADD:
                // If new ingredients have been added, then search through all recipes that
                // currently have missing ingredients. If a recipe was missing one of the
                // ingredients added, then remove that ingredient from the collection of missing
                // ones. If a recipe is no longer missing any ingredients, mark it as such
                Log.i(TAG, "Updating recipes due to addition of ingredients - Start");
                boolean change = false;
                for (final Map.Entry<Recipe, ConcurrentSkipListSet<Ingredient>> entry : lackingRecipes.entrySet()) {
                    if (entry.getValue().removeAll(ingredients) && entry.getValue().isEmpty()) {
                        change = true;
                        recipesForCurrent.add(entry.getKey());
                        lackingRecipes.remove(entry.getKey());
                    }
                }

                if (change)
                    RecipeData.manager.markChangeInRecipesForCurrentIngredients();
                Log.i(TAG, "Updating recipes due to addition of ingredients - End");
                break;

            case REMOVE:
                // If ingredients are being removed, then there are two scenarios to deal with.
                // Firstly, any complete recipes that require the removed ingredient must now be
                // marked as incomplete. Second, any incomplete recipe which also requires this
                // ingredient must be updated to indicate that it is additionally missing the
                // current ingredient.
                Log.i(TAG, "Updating recipes due to removal of ingredients - Start");

                ConcurrentSkipListSet<Ingredient> missingIngredients;
                for (final Map.Entry<Recipe, ConcurrentSkipListSet<Ingredient>> entry : lackingRecipes.entrySet()) {
                    missingIngredients = new ConcurrentSkipListSet<>(ingredients);
                    missingIngredients.retainAll(entry.getKey().getAllIngredients());
                    entry.getValue().addAll(missingIngredients);
                }

                for (final Recipe recipe : recipesForCurrent) {
                    missingIngredients = new ConcurrentSkipListSet<>(ingredients);
                    missingIngredients.retainAll(recipe.getAllIngredients());
                    if (!missingIngredients.isEmpty()) {
                        lackingRecipes.put(recipe, missingIngredients);
                        recipesForCurrent.remove(recipe);
                    }
                }

                RecipeData.manager.markChangeInRecipesForCurrentIngredients();
                Log.i(TAG, "Updating recipes due to removal of ingredients - End");
        }

        return ingredients;
    }
}

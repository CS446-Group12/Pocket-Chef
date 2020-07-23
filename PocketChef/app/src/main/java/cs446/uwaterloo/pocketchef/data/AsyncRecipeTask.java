package cs446.uwaterloo.pocketchef.data;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class AsyncRecipeTask extends AsyncTask<Recipe, Integer, Recipe[]> {
    public enum Action { ADD, REMOVE }

    private final String TAG = "AsyncRecipeTask";

    private Action action;

    public AsyncRecipeTask(Action desiredAction) {
        super();
        action = desiredAction;
    }

    @Override
    protected Recipe[] doInBackground(Recipe... recipeArray) {
        ConcurrentSkipListSet<Recipe> recipesForCurrent
                = RecipeData.manager.getRecipesForCurrentIngredients();
        ConcurrentSkipListMap<Recipe, ConcurrentSkipListSet<Ingredient>> lackingRecipes
                = RecipeData.manager.getRecipesWithMissingIngredients();

        switch (action) {
            case ADD:
                // Adding recipes requires iteration through the present ingredients. This will
                // determine which ingredients are not present for the current recipe. If there are
                // none missing, then the recipe will be marked as complete. If there are some
                // missing, then the recipe will be marked as complete and a set of all missing
                // ingredients will be created.
                Log.i(TAG, "Updating recipes due to addition of new recipe - Start");

                ConcurrentSkipListSet<Ingredient> ingredientsUsed;
                for (final Recipe recipe : recipeArray) {
                    ingredientsUsed = new ConcurrentSkipListSet<>(recipe.getAllIngredients());
                    ingredientsUsed.removeAll(IngredientData.manager.sortedData);
                    if (ingredientsUsed.isEmpty())
                        recipesForCurrent.add(recipe);
                    else
                        lackingRecipes.put(recipe, ingredientsUsed);
                }

                RecipeData.manager.markChangeInRecipesForCurrentIngredients();
                Log.i(TAG, "Updating recipes due to addition of new recipe - End");
                break;

            case REMOVE:
                // Removing a recipe requires that any reference to it in either the
                // recipesForCurrent or lackingRecipes
                Log.i(TAG, "Updating recipes due to removal of recipe - Start");

                for (final Recipe recipe : recipeArray) {
                    recipesForCurrent.remove(recipe);
                    lackingRecipes.remove(recipe);
                }

                RecipeData.manager.markChangeInRecipesForCurrentIngredients();
                Log.i(TAG, "Updating recipes due to removal of recipe - End");
        }

        return recipeArray;
    }
}

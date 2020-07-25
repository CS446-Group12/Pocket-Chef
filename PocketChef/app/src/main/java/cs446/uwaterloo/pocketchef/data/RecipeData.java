package cs446.uwaterloo.pocketchef.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class RecipeData extends BaseDataManager<Recipe> {
    private static String TAG = "RecipeData";

    private boolean recipesForCurrentIngredientsShown = false;
    private ConcurrentSkipListSet<Recipe> recipesForCurrentIngredients;
    private ConcurrentSkipListMap<Recipe, ConcurrentSkipListSet<Ingredient>>
            recipesWithMissingIngredients;

    public static final RecipeData manager = new RecipeData(Recipe.getDemoRecipes());

    private RecipeData(Collection<Recipe> recipes) {
        super(recipes);
        recipesForCurrentIngredients = new ConcurrentSkipListSet<>();
        recipesWithMissingIngredients = new ConcurrentSkipListMap<>();
    }

    public ConcurrentSkipListSet<Recipe> getRecipesForCurrentIngredients() {
        return recipesForCurrentIngredients;
    }

    public ConcurrentSkipListMap<Recipe, ConcurrentSkipListSet<Ingredient>>
        getRecipesWithMissingIngredients() {
            return recipesWithMissingIngredients;
    }

    public void markChangeInRecipesForCurrentIngredients() {
        if (recipesForCurrentIngredientsShown) {
            displayData = new ArrayList<>(recipesForCurrentIngredients);
            adapter.notifyDataSetChanged();
        }
    }

    public void switchShownRecipes() {
        Log.i(TAG, "Switching shown recipes");
        recipesForCurrentIngredientsShown ^= true;
        resetDisplayData();

        if (filterKeyword != null && !filterKeyword.isEmpty())
            filterByName(filterKeyword);
    }

    public void showRecipesForCurrentIngredients() {
        Log.i(TAG, "Showing recipes only for current ingredients");
        displayData = new ArrayList<>(recipesForCurrentIngredients);
        recipesForCurrentIngredientsShown = true;
        adapter.notifyDataSetChanged();
    }

    public ArrayList<Recipe> getDisplayRecipes() {
        return displayData;
    }

    public ConcurrentSkipListSet<Recipe> getAllRecipes() {
        return sortedData;
    }

    public void resetRecipes() {
        Log.i(TAG, "Resetting currently shown recipes");
        resetDisplayData();
    }

    @Override
    protected void resetDisplayData() {
        if (recipesForCurrentIngredientsShown) {
            showRecipesForCurrentIngredients();
        } else {
            displayDataIsModified = true;
            super.resetDisplayData();
        }
    }

    public void addRecipe(Recipe... recipes) {
        addRecipe(Arrays.asList(recipes));
    }

    public void addRecipe(Collection<Recipe> recipes) {
        Log.i(TAG, "Adding recipes: " + Arrays.toString(recipes.toArray()));
        addData(recipes);
        new AsyncRecipeTask(AsyncRecipeTask.Action.ADD).execute(recipes.toArray(new Recipe[0]));
    }

    public void removeRecipes(Recipe... recipes) {
        removeRecipes(Arrays.asList(recipes));
    }

    public void removeRecipes(Collection<Recipe> recipes) {
        Log.i(TAG, "Removing recipes: " + Arrays.toString(recipes.toArray()));
        removeData(recipes);
        new AsyncRecipeTask(AsyncRecipeTask.Action.REMOVE).execute(recipes.toArray(new Recipe[0]));
    }
}

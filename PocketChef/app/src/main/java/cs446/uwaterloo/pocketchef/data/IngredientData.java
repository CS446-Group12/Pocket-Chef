package cs446.uwaterloo.pocketchef.data;

import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import cs446.uwaterloo.pocketchef.adapters.IngredientAdapter;
import cs446.uwaterloo.pocketchef.model.Ingredient;

public class IngredientData {
    private final static String TAG = "IngredientData";

    private static boolean initialUpdateComplete = false;
    private static IngredientAdapter adapter;
    private static List<Ingredient> currentIngredients;
    private static HashSet<Ingredient> allIngredients;

    public static final IngredientData manager = new IngredientData(Ingredient.createIngredientsList());

    private IngredientData(List<Ingredient> ingredients) {
        adapter = null;
        currentIngredients = ingredients;
        allIngredients = new HashSet<>(ingredients);
    }

    public static void setAdapter(IngredientAdapter newAdapter) {
        adapter = newAdapter;
        if (!initialUpdateComplete) {
            new AsyncRecipeMap(AsyncRecipeMap.Action.INITIALIZE)
                    .execute(allIngredients.toArray(new Ingredient[0]));
            initialUpdateComplete = true;
        }
    }

    public static List<Ingredient> getCurrentIngredients() {
        return currentIngredients;
    }

    protected static HashSet<Ingredient> getAllIngredients() {
        return allIngredients;
    }

    public static void resetIngredients() {
        Log.i(TAG, "Resetting currentIngredients");
        HashSet<Ingredient> diff = new HashSet<>(allIngredients);
        diff.removeAll(currentIngredients);
        int index = currentIngredients.size();
        currentIngredients.addAll(diff);
        adapter.notifyItemRangeInserted(index, diff.size());
    }

    public static void hideIngredients(Ingredient... ingredients) {
        hideIngredients(Arrays.asList(ingredients));
    }

    public static void hideIngredients(List<Ingredient> ingredients) {
        ingredients.retainAll(allIngredients);
        int index;
        for (final Ingredient ingredient : ingredients) {
            if ((index = currentIngredients.indexOf(ingredient)) != -1) {
                Log.i(TAG, "Hiding ingredient: " + ingredient.getName());
                currentIngredients.remove(index);
                adapter.notifyItemRemoved(index);
            }
        }
    }

    public static void revealIngredients(Ingredient... ingredients) {
        revealIngredients(Arrays.asList(ingredients));
    }

    public static void revealIngredients(List<Ingredient> ingredients) {
        ingredients.retainAll(allIngredients);
        int index = 0;
        while (index < ingredients.size()) {
            if (currentIngredients.contains(ingredients.get(index)))
                ingredients.remove(index);
            else
                index++;
        }

        Log.i(TAG, "Revealing ingredients: " + Arrays.toString(ingredients.toArray()));
        index = currentIngredients.size();
        currentIngredients.addAll(ingredients);
        adapter.notifyItemRangeInserted(index, ingredients.size());
    }

    public static void removeIngredients(Ingredient... ingredients) {
        removeIngredients(Arrays.asList(ingredients));
    }

    public static void removeIngredients(List<Ingredient> ingredients) {
        hideIngredients(ingredients);
        Log.i(TAG, "Removing ingredients: " + Arrays.toString(ingredients.toArray()));
        allIngredients.removeAll(ingredients);
        new AsyncRecipeMap(AsyncRecipeMap.Action.REMOVE)
                .execute(ingredients.toArray(new Ingredient[0]));
    }

    public static void addIngredients(boolean reveal, Ingredient... ingredients) {
        addIngredients(reveal, Arrays.asList(ingredients));
    }

    public static void addIngredients(boolean reveal, List<Ingredient> ingredients) {
        Log.i(TAG, "Adding ingredients: " + Arrays.toString(ingredients.toArray()));
        allIngredients.addAll(ingredients);
        if (reveal)
            revealIngredients(ingredients);
        new AsyncRecipeMap(AsyncRecipeMap.Action.ADD)
                .execute(ingredients.toArray(new Ingredient[0]));
    }
}

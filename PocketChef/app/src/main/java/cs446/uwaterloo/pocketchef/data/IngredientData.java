package cs446.uwaterloo.pocketchef.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListSet;

import cs446.uwaterloo.pocketchef.adapters.IngredientAdapter;
import cs446.uwaterloo.pocketchef.model.Ingredient;

public class IngredientData extends BaseDataManager<Ingredient> {
    private final static String TAG = "IngredientData";

    private boolean initialUpdateComplete = false;

    public static final IngredientData manager =
            new IngredientData(Ingredient.createIngredientsList());

    private IngredientData(Collection<Ingredient> ingredients) {
        super(ingredients);
    }

    public void setAdapter(IngredientAdapter newAdapter) {
        super.setAdapter(newAdapter);
        if (!initialUpdateComplete) {
            new AsyncIngredientTask(AsyncIngredientTask.Action.INITIALIZE)
                    .execute((Ingredient[]) sortedData.toArray(new Ingredient[0]));
            initialUpdateComplete = true;
        }
    }

    public ArrayList<Ingredient> getDisplayIngredients() {
        return displayData;
    }

    public ConcurrentSkipListSet<Ingredient> getAllIngredients() {
        return sortedData;
    }

    public void resetIngredients() {
        Log.i(TAG, "Resetting currently shown ingredients");
        resetDisplayData();
    }

    public void addIngredients(Ingredient... ingredients) {
        addIngredients(Arrays.asList(ingredients));
    }

    public void addIngredients(Collection<Ingredient> ingredients) {
        Log.i(TAG, "Adding ingredients: " + Arrays.toString(ingredients.toArray()));
        addData(ingredients);
        new AsyncIngredientTask(AsyncIngredientTask.Action.ADD)
                .execute(ingredients.toArray(new Ingredient[0]));
    }

    public void removeIngredients(Ingredient... ingredients) {
        removeIngredients(Arrays.asList(ingredients));
    }

    public void removeIngredients(Collection<Ingredient> ingredients) {
        Log.i(TAG, "Removing ingredients: " + Arrays.toString(ingredients.toArray()));
        removeData(ingredients);
        new AsyncIngredientTask(AsyncIngredientTask.Action.REMOVE)
                .execute(ingredients.toArray(new Ingredient[0]));
    }
}

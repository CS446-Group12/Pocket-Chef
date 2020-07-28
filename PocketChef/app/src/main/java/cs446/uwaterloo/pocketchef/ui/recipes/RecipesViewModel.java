package cs446.uwaterloo.pocketchef.ui.recipes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.HashSet;
import java.util.List;

import cs446.uwaterloo.pocketchef.data.CookingDatabase;
import cs446.uwaterloo.pocketchef.data.IngredientDao;
import cs446.uwaterloo.pocketchef.data.RecipeDao;
import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.RecipeAndCounts;

public class RecipesViewModel extends AndroidViewModel {

    // TODO figure out what to do with this limit
    private static final int MAX_MISSING_INGREDIENTS = 2;
    private static final int ALL_RECIPE_LIMIT = 5000;
    private static final int AVAILABLE_RECIPE_LIMIT = 1000;

    private CookingDatabase db;

    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;

    private HashSet<Integer> availableIngredientIds;
    private LiveData<List<RecipeAndCounts>> availableRecipes;

    private LiveData<List<RecipeAndCounts>> allRecipes;

    private Observer<List<Ingredient>> ingredientObserver = new Observer<List<Ingredient>>() {
        @Override
        public void onChanged(List<Ingredient> ingredients) {
            availableIngredientIds.clear();
            for (Ingredient ingredient : ingredients) {
                availableIngredientIds.add(ingredient.id);
            }
            // refresh the available recipes
            getAvailableRecipes(true);
            getAllRecipes(true);
        }
    };

    public RecipesViewModel(Application application) {
        super(application);
        db = CookingDatabase.getDatabase(application.getApplicationContext());
        recipeDao = db.recipeDao();
        ingredientDao = db.ingredientDao();

        availableIngredientIds = new HashSet<>();

        ingredientDao.getAvailableIngredients().observeForever(ingredientObserver);
    }

    public LiveData<List<RecipeAndCounts>> getAvailableRecipes() {
        return getAvailableRecipes(false);
    }

    public LiveData<List<RecipeAndCounts>> getAvailableRecipes(boolean refresh) {
        if (availableRecipes == null || refresh) {
            availableRecipes = recipeDao.getSuitableRecipes(availableIngredientIds, MAX_MISSING_INGREDIENTS, AVAILABLE_RECIPE_LIMIT);
        }
        return availableRecipes;
    }

    public LiveData<List<RecipeAndCounts>> getAllRecipes() {
        return getAllRecipes(false);
    }

    public LiveData<List<RecipeAndCounts>> getAllRecipes(boolean refresh) {
        if (allRecipes == null || refresh) {
            allRecipes = recipeDao.getAllRecipesAndCounts(ALL_RECIPE_LIMIT);
        }
        return allRecipes;
    }

    @Override
    protected void onCleared() {
        ingredientDao.getAvailableIngredients().removeObserver(ingredientObserver);
        super.onCleared();
    }

}
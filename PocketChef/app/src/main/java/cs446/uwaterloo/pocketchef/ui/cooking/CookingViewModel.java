package cs446.uwaterloo.pocketchef.ui.cooking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import cs446.uwaterloo.pocketchef.data.CookingDatabase;
import cs446.uwaterloo.pocketchef.data.IngredientDao;
import cs446.uwaterloo.pocketchef.data.RecipeDao;
import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;
import cs446.uwaterloo.pocketchef.model.RecipeAndIngredients;

public class CookingViewModel extends AndroidViewModel {
    private CookingDatabase db;

    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;

    public CookingViewModel(@NonNull Application application) {
        super(application);
        db = CookingDatabase.getDatabase(application.getApplicationContext());
        recipeDao = db.recipeDao();
        ingredientDao = db.ingredientDao();
    }

    private LiveData<Recipe> recipe = new MediatorLiveData<>();

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public void updateAndSetRecipe(final Recipe recipe) {
        CookingDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.updateRecipes(recipe);
            }
        });
        this.recipe = recipeDao.getRecipeById(recipe.ID);
    }

    public LiveData<RecipeAndIngredients> getRecipeAndIngredients(Recipe recipe) {
        return recipeDao.getRecipeAndIngredients(recipe.ID);
    }

    public void updateIngredient(Ingredient ingredient) {
        ingredientDao.updateIngredient(ingredient);
    }

    public void updateIngredients(final List<Ingredient> ingredients) {
        CookingDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (Ingredient ingredient : ingredients) {
                    updateIngredient(ingredient);
                }
            }
        });
    }
}
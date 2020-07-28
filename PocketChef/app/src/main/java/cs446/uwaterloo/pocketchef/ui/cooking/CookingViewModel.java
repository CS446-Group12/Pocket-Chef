package cs446.uwaterloo.pocketchef.ui.cooking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import cs446.uwaterloo.pocketchef.data.CookingDatabase;
import cs446.uwaterloo.pocketchef.data.RecipeDao;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class CookingViewModel extends AndroidViewModel {

    private MutableLiveData<String> mainText = new MutableLiveData<>();

    private CookingDatabase db;

    private RecipeDao recipeDao;

    public CookingViewModel(@NonNull Application application) {
        super(application);
        db = CookingDatabase.getDatabase(application.getApplicationContext());
        recipeDao = db.recipeDao();
    }


    public LiveData<String> getMainText() {
        return mainText;
    }

    public void setMainText(String text) {
        this.mainText.setValue(text);
    }

    private LiveData<Recipe> recipe = new MediatorLiveData<>();

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public void setRecipe(final Recipe recipe) {
        CookingDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.updateRecipes(recipe);
            }
        });
        this.recipe = recipeDao.getRecipeById(recipe.ID);
    }

}
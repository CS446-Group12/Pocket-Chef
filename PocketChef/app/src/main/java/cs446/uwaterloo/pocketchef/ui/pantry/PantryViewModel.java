package cs446.uwaterloo.pocketchef.ui.pantry;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import cs446.uwaterloo.pocketchef.data.CookingDatabase;
import cs446.uwaterloo.pocketchef.data.IngredientDao;
import cs446.uwaterloo.pocketchef.model.Ingredient;

public class PantryViewModel extends AndroidViewModel {

    private CookingDatabase db;

    private IngredientDao ingredientDao;

    private LiveData<List<Ingredient>> availableIngredients;

    private LiveData<List<Ingredient>> allIngredients;

    public PantryViewModel(Application application) {
        super(application);
        db = CookingDatabase.getDatabase(application.getApplicationContext());
        ingredientDao = db.ingredientDao();
        availableIngredients = ingredientDao.getAvailableIngredients();
    }

    public LiveData<List<Ingredient>> getAvailableIngredients() {
        return availableIngredients;
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        if (allIngredients == null) {
            allIngredients = ingredientDao.getAllIngredients();
        }
        return allIngredients;
    }

    public void filterAllIngredients(String namePattern) {
        if (namePattern == null || namePattern.length() == 0) {
            allIngredients = ingredientDao.getAllIngredients();
        } else {
            allIngredients = ingredientDao.getAllIngredients(namePattern);
        }
    }


    public void updateIngredient(final Ingredient... ingredients) {
        CookingDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (Ingredient i : ingredients) {
                    ingredientDao.updateIngredient(i);
                }
            }
        });
    }

    public LiveData<List<Ingredient>> filterAvailableIngredients(String namePattern) {
        if (namePattern == null || namePattern.length() == 0) {
            availableIngredients = ingredientDao.getAvailableIngredients();
        } else {
            availableIngredients = ingredientDao.getAvailableIngredients(namePattern);
        }
        return availableIngredients;
    }

    public Ingredient findIngredientByName(String name) {
        return ingredientDao.findIngredientByName(name);
    }
}
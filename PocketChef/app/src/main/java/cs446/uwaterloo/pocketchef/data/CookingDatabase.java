package cs446.uwaterloo.pocketchef.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;
import cs446.uwaterloo.pocketchef.model.RecipeIngredientCount;
import cs446.uwaterloo.pocketchef.model.RecipeIngredientsAssociation;

@Database(version = 1, entities = {
        Ingredient.class,
        Recipe.class,
        RecipeIngredientsAssociation.class,
}, views = {
        RecipeIngredientCount.class,
})
public abstract class CookingDatabase extends RoomDatabase {

    public abstract IngredientDao ingredientDao();

    public abstract RecipeDao recipeDao();

    private static volatile CookingDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS / 2);
    public static final ExecutorService databaseSearchExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS / 2);

    public static CookingDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CookingDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            CookingDatabase.class,
                            "recipes.db"
                    )
                            .createFromAsset("recipes.db")
                            .build();
                }
            }
            Log.i("DB", "DB at " + context.getDatabasePath("recipe.db"));
        }
        return INSTANCE;
    }

}


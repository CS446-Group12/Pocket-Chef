package cs446.uwaterloo.pocketchef.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Collection;
import java.util.List;

import cs446.uwaterloo.pocketchef.model.Recipe;
import cs446.uwaterloo.pocketchef.model.RecipeAndCounts;
import cs446.uwaterloo.pocketchef.model.RecipeAndIngredients;

@Dao
public interface RecipeDao {
    @Transaction
    @Query("SELECT r.*, count(r.id) as matches, rc.ingredientCount - count(r.id) AS numMissing, rc.ingredientCount " +
            "FROM RECIPE r INNER JOIN RECIPE_INGREDIENT ri ON r.ID = ri.recipe_id " +
            "    INNER JOIN RECIPE_INGREDIENT_COUNT rc on r.ID = rc.recipe_id " +
            "GROUP BY r.ID " +
            "ORDER BY numMissing ASC " +
            "LIMIT :count")
    LiveData<List<RecipeAndCounts>> getAllRecipesAndCounts(int count);

    @Transaction
    @Query("SELECT r.*, " +
           "    COUNT(ri.ingredient_id) as ingredientCount, " +
           "    COUNT(NULLIF(i.stock, 0)) as matches, " +
           "    COUNT(ri.ingredient_id) - COUNT(NULLIF(i.stock, 0)) as numMissing " +
           "FROM RECIPE r " +
           "    INNER JOIN RECIPE_INGREDIENT ri ON r.ID = ri.recipe_id " +
           "    INNER JOIN INGREDIENT i ON i.ID = ri.ingredient_id " +
           "GROUP BY r.ID " +
           "HAVING numMissing <= :maxNumberOfMissingIngredients AND matches > numMissing " +
           "ORDER BY numMissing ASC, matches DESC " +
           "LIMIT :count"
    )
    LiveData<List<RecipeAndCounts>>
    getSuitableRecipes(int maxNumberOfMissingIngredients, int count);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipes(Recipe... recipes);

    @Query("SELECT * FROM RECIPE WHERE ID = :id LIMIT 1;")
    LiveData<Recipe> getRecipeById(int id);

    @Transaction
    @Query("SELECT * FROM " +
            "RECIPE " +
            "WHERE ID = :recipeId")
    LiveData<RecipeAndIngredients> getRecipeAndIngredients(int recipeId);
}

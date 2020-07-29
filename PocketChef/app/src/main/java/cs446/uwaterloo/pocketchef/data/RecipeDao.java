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
    @Query("SELECT r.*, count(r.id) as matches, rc.ingredientCount - count(r.id) AS numMissing, rc.ingredientCount " +
            "FROM RECIPE r INNER JOIN RECIPE_INGREDIENT ri ON r.ID = ri.recipe_id " +
            "    INNER JOIN RECIPE_INGREDIENT_COUNT rc on r.ID = rc.recipe_id " +
            "WHERE ri.ingredient_id IN(:ingredientIds)" +
            "GROUP BY r.ID " +
            "HAVING numMissing <= :maxNumberOfMissingIngredients " +
            "ORDER BY numMissing ASC " +
            "LIMIT :count")
    LiveData<List<RecipeAndCounts>>
    getSuitableRecipes(Collection<Integer> ingredientIds, int maxNumberOfMissingIngredients, int count);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipes(Recipe... recipes);

    @Query("SELECT * FROM RECIPE WHERE ID = :id LIMIT 1;")
    LiveData<Recipe> getRecipeById(int id);
}

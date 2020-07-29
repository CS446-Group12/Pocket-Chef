package cs446.uwaterloo.pocketchef.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import cs446.uwaterloo.pocketchef.model.Ingredient;

@Dao
public interface IngredientDao {
    @Query("SELECT * FROM INGREDIENT WHERE ID = :ingredient_id LIMIT 1")
    Ingredient getIngredient(int ingredient_id);

    @Query("SELECT * FROM INGREDIENT WHERE stock > 0")
    LiveData<List<Ingredient>> getAvailableIngredients();

    // TODO using LIKE matching isn't intuitive
    @Query("SELECT * FROM INGREDIENT WHERE stock > 0 AND NAME LIKE :namePattern")
    LiveData<List<Ingredient>> getAvailableIngredients(String namePattern);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateIngredient(Ingredient ingredient);

    @Query("SELECT * FROM INGREDIENT")
    LiveData<List<Ingredient>> getAllIngredients();

    @Query("SELECT * FROM INGREDIENT WHERE NAME LIKE :namePattern")
    LiveData<List<Ingredient>> getAllIngredients(String namePattern);

    @Query("SELECT * FROM INGREDIENT WHERE NAME = :name LIMIT 1")
    Ingredient findIngredientByName(String name);
}

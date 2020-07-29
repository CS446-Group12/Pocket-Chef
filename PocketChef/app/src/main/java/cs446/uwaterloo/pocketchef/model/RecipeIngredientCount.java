package cs446.uwaterloo.pocketchef.model;

import androidx.room.DatabaseView;

@DatabaseView(value = "SELECT r.ID as recipe_id, COUNT(ri.ingredient_id) as ingredientCount " +
        "FROM RECIPE r INNER JOIN RECIPE_INGREDIENT ri ON r.ID = ri.recipe_id " +
        "GROUP BY r.ID", viewName = "RECIPE_INGREDIENT_COUNT")
public class RecipeIngredientCount {
    public int recipe_id;
    public int ingredientCount;
}

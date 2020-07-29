package cs446.uwaterloo.pocketchef.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RecipeAndIngredients {
    @Embedded public Recipe recipe;
    @Relation(
            parentColumn = "ID",
            entityColumn = "ID",
            associateBy = @Junction(
                    value = RecipeIngredientsAssociation.class,
                    parentColumn = "recipe_id",
                    entityColumn = "ingredient_id"
            )
    )
    public List<Ingredient> ingredients;
}

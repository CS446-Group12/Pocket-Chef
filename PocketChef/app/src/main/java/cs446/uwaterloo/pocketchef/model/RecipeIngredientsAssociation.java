package cs446.uwaterloo.pocketchef.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "RECIPE_INGREDIENT",
        primaryKeys = {"recipe_id", "ingredient_id"},
        foreignKeys = {
                @ForeignKey(entity = Recipe.class,
                        parentColumns = "ID", childColumns = "recipe_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Ingredient.class,
                        parentColumns = "ID", childColumns = "ingredient_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"recipe_id", "ingredient_id"}, unique = true)
        }
)
public class RecipeIngredientsAssociation {
    @NonNull
    @ColumnInfo(index = true)
    public long recipe_id;
    @NonNull
    @ColumnInfo(index = true)
    public long ingredient_id;
}

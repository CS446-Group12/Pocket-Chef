package cs446.uwaterloo.pocketchef.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class RecipeAndCounts {
    @Embedded
    public Recipe recipe;

    @ColumnInfo(name = "matches")
    public int satisfiedIngredientCount;
    @ColumnInfo(name = "numMissing")
    public int missingIngredientCount;
    @ColumnInfo(name = "ingredientCount")
    public int ingredientCount;
}

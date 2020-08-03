package cs446.uwaterloo.pocketchef.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Date;

@Entity(
        tableName = "CONSUMPTION",
        primaryKeys = {"ingredient_id", "date"},
        foreignKeys = {
                @ForeignKey(entity = Ingredient.class,
                        parentColumns = "ID", childColumns = "ingredient_id",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Consumption {
    public int ingredient_id;
    @NonNull
    public Date date;
    public double amount;
}

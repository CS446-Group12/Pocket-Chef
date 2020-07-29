package cs446.uwaterloo.pocketchef.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "INGREDIENT")
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID", index = true)
    public int id;

    @ColumnInfo(name = "NAME", index = true)
    @NonNull
    public String name;

    @ColumnInfo(name = "Stock", defaultValue = "0")
    public double stock;

    public Ingredient(int id, @NonNull String name, double stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    @NonNull
    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                '}';
    }
}

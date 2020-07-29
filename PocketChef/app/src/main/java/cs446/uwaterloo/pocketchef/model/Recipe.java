package cs446.uwaterloo.pocketchef.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RECIPE")
public class Recipe implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int ID;

    @ColumnInfo(name = "title")
    @NonNull
    public String title;

    @ColumnInfo(name = "desc")
    public String description;

    @ColumnInfo(name = "fat")
    public Integer fat;

    @ColumnInfo(name = "calories")
    public Integer calories;

    @ColumnInfo(name = "protein")
    public Integer protein;

    @ColumnInfo(name = "rating", defaultValue = "0")
    @NonNull
    public Float rating;

    @ColumnInfo(name = "sodium")
    public Integer sodium;

    @ColumnInfo(name = "ingredients")
    @NonNull
    public String ingredientText;

    @ColumnInfo(name = "directions")
    @NonNull
    public String directionText;

    public String[] getIngredients() {
        return ingredientText.split("\0");
    }

    public String[] getDirections() {
        return directionText.split("\0");
    }

    /////////////////////////////////////////////////////////////////////////////
    /// Auto generated

    public Recipe(int ID, @NonNull String title, String description, Integer fat, Integer calories, Integer protein, @NonNull Float rating, Integer sodium, @NonNull String ingredientText, @NonNull String directionText) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.fat = fat;
        this.calories = calories;
        this.protein = protein;
        this.rating = rating;
        this.sodium = sodium;
        this.ingredientText = ingredientText;
        this.directionText = directionText;
    }

    protected Recipe(Parcel in) {
        ID = in.readInt();
        title = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            fat = null;
        } else {
            fat = in.readInt();
        }
        if (in.readByte() == 0) {
            calories = null;
        } else {
            calories = in.readInt();
        }
        if (in.readByte() == 0) {
            protein = null;
        } else {
            protein = in.readInt();
        }
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readFloat();
        }
        if (in.readByte() == 0) {
            sodium = null;
        } else {
            sodium = in.readInt();
        }
        ingredientText = in.readString();
        directionText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(title);
        dest.writeString(description);
        if (fat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(fat);
        }
        if (calories == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(calories);
        }
        if (protein == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(protein);
        }
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(rating);
        }
        if (sodium == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sodium);
        }
        dest.writeString(ingredientText);
        dest.writeString(directionText);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return ID;
    }
}

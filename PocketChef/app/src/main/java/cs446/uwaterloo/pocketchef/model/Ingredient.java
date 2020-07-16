package cs446.uwaterloo.pocketchef.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

public class Ingredient implements Parcelable {

    private String name;
    private Date expirationDate;

    public Ingredient(String newName) {

        this.name = newName;

    }

    @NonNull
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void setExpirationDate(Date newExpirationDate) {

        this.expirationDate = newExpirationDate;

    }

    public String getName() {

        return this.name;

    }

    public Date getExpirationDate() {

        return this.expirationDate;

    }

    //Method to quickly create a sample list of ingredients for testing purposes
    public static ArrayList<Ingredient> createIngredientsList() {

        ArrayList<Ingredient> result = new ArrayList<Ingredient>();
        result.add(new Ingredient("Tomatoes"));
        result.add(new Ingredient("Cheese"));
        result.add(new Ingredient("Potatoes"));
        result.add(new Ingredient("Sausages"));
        result.add(new Ingredient("Chicken"));
        result.add(new Ingredient("Ketchup"));
        result.add(new Ingredient("Garlic"));
        result.add(new Ingredient("Chili peppers"));
        result.add(new Ingredient("Beef"));
        result.add(new Ingredient("Lamb"));
        result.add(new Ingredient("Pork"));
        result.add(new Ingredient("Bacon"));
        result.add(new Ingredient("Salmon"));
        result.add(new Ingredient("Milk"));
        result.add(new Ingredient("Coffee"));
        result.add(new Ingredient("Tequila"));
        result.add(new Ingredient("Triple Sec"));
        result.add(new Ingredient("Lime juice"));

        return result;

    }

    /* Code for implementing Parcelable starts here */
    @Override
    public int describeContents() {
        return 0;
    }

    // Write object data to parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);

        if (expirationDate != null) {
            out.writeLong(expirationDate.getTime());
        } else {
            out.writeLong(Long.MIN_VALUE);
        }
    }

    // Used to regenerate object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Ingredient> CREATOR
            = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    // Ingredient constructor with Parcel parameter
    private Ingredient(Parcel in) {
        name = in.readString();
        Long dateValue = in.readLong();
        if (dateValue != Long.MIN_VALUE) {
            expirationDate = new Date(in.readLong());
        }
    }
    /* Code for implementing Parcelable ends here */
}

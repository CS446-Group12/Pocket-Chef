package cs446.uwaterloo.pocketchef.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Ingredient extends BaseModel implements Parcelable {
    private Date expirationDate;

    public Ingredient(String newName) {
        this(newName, null);
    }

    public Ingredient(String newName, Date date) {
        super(newName);
        this.expirationDate = date;
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

    /**
     * set a random expiration date for the demo
     */
    public void setRandomExpirationDate() {
        Calendar calendar = Calendar.getInstance();

        // set expiration date to 14 days in the future
        // with +-5 days of variability
        int day = calendar.get(Calendar.DATE);
        int offset = (int) (5 * 2 * (Math.random() - 0.5));
        int final_date = calendar.get(Calendar.DATE) + 14 + offset;
        calendar.set(Calendar.DATE, final_date);
        this.expirationDate = calendar.getTime();
    }

    public Date getExpirationDate() {

        return this.expirationDate;

    }

    //Method to quickly create a sample list of ingredients for testing purposes
    public static ArrayList<Ingredient> createIngredientsList() {

        ArrayList<Ingredient> result = new ArrayList<>();
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

        for (Ingredient ingredient : result) {
            ingredient.setRandomExpirationDate();
        }
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
        super(in.readString());
        long dateValue = in.readLong();
        if (dateValue != Long.MIN_VALUE) {
            expirationDate = new Date(dateValue);
        }
    }
    /* Code for implementing Parcelable ends here */
}

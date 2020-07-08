package cs446.uwaterloo.pocketchef.model;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

public class Ingredient {

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
        return Objects.equals(name, that.name) &&
                Objects.equals(expirationDate, that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, expirationDate);
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

}

package cs446.uwaterloo.pocketchef.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

// Parcelable is similar to Java's Serializable. It is more efficient but requires a bit more code
public class Recipe implements Parcelable {
    private String name;
    private HashSet<Ingredient> ingredients;
    private Rating rating;
    private ArrayList<String> steps;

    public enum Rating { ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS, FIVE_STARS }

    public Recipe(String name, Ingredient... ingredients) {
        this(name, Arrays.asList(ingredients), null);
    }

    public Recipe(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = new HashSet<>(ingredients);
        this.steps = null;
    }

    public Recipe(String name, List<Ingredient> ingredients, List<String> steps) {
        this.name = name;
        this.ingredients = new HashSet<>(ingredients);
        this.steps = new ArrayList<>(steps);
    }

    @NonNull
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(name, recipe.name) &&
                Objects.equals(ingredients, recipe.ingredients) &&
                rating == recipe.rating &&
                Objects.equals(steps, recipe.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients, rating);
    }

    public HashSet<Ingredient> getAllIngredients() {
        return ingredients;
    }

    public String getName() {
        return name;
    }

    public Rating getRating() {
        return rating;
    }

    public List<String> getSteps() { return steps; }

    public void updateName(String newName) {
        name = newName;
    }

    public void updateRating(Rating newRating) {
        rating = newRating;
    }

    public Boolean isIngredient(Ingredient in) {
        return ingredients.contains(in);
    }

    public Boolean areIngredients(Ingredient... in) {
        return ingredients.containsAll(Arrays.asList(in));
    }

    public static ArrayList<Recipe> getDemoRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Beef Chili",
                Arrays.asList(new Ingredient("Beef"), new Ingredient("Chili peppers"), new Ingredient("Tomatoes")),
                Arrays.asList("Add the ground beef to the pot. Break it apart with a wooden spoon. Cook for 6-7 minutes, until the beef is browned, stirring occasionally.",
                        "Add the chili peppers, and tomatoes. Stir until well combined.",
                        "Bring the liquid to a low boil. Then, reduce the heat (low to medium-low) to gently simmer the chili, uncovered, for 20-25 minutes, stirring occasionally.",
                        "Remove the pot from the heat. Let the chili rest for 5-10 minutes before serving.")));
        recipes.add(new Recipe("Bacon-Wrapped Chicken",
                Arrays.asList(new Ingredient("Chicken"), new Ingredient("Bacon"), new Ingredient("Tomatoes")),
                Arrays.asList("a", "b", "c")));
        recipes.add(new Recipe("Pigs in a Blanket",
                Arrays.asList(new Ingredient("Sausages"), new Ingredient("Bacon")),
                Arrays.asList("a", "b", "c")));
        recipes.add(new Recipe("Tequila Shot",
                Arrays.asList(new Ingredient("Tequila"), new Ingredient("Lime juice")),
                Arrays.asList("a", "b", "c")));
        recipes.add(new Recipe("Grilled Cheese",
                Arrays.asList(new Ingredient("Cheese"), new Ingredient("Bread")),
                Arrays.asList("a", "b", "c")));
        for (Recipe r : recipes) {
            for (Ingredient i : r.ingredients) {
                i.setRandomExpirationDate();
            }
        }
        return recipes;
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
        out.writeTypedList(new ArrayList<>(ingredients));
        if (rating != null) {
            out.writeString(rating.name());
        } else {
            out.writeString("");
        }
        out.writeStringList(steps);
    }

    // Used to regenerate object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    // Recipe constructor with Parcel parameter
    private Recipe(Parcel in) {
        name = in.readString();
        ingredients = new HashSet<>(in.createTypedArrayList(Ingredient.CREATOR));
        String ratingValue = in.readString();
        if (ratingValue != null && !ratingValue.isEmpty()) {
            rating = Rating.valueOf(ratingValue);
        }
        steps = in.createStringArrayList();
    }
    /* Code for implementing Parcelable ends here */
}

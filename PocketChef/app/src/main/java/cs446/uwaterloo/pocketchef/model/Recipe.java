package cs446.uwaterloo.pocketchef.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class Recipe {
    private String name;
    private HashSet<Ingredient> ingredients;
    private Rating rating;

    public enum Rating { ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS, FIVE_STARS }

    public Recipe(String name, Ingredient... ingredients) {
        this(name, Arrays.asList(ingredients));
    }

    public Recipe(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = new HashSet<>(ingredients);
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
                rating == recipe.rating;
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
        recipes.add(new Recipe("Beef Chili", new Ingredient("Beef"), new Ingredient("Chili peppers"), new Ingredient("Tomatoes")));
        recipes.add(new Recipe("Bacon-Wrapped Chicken", new Ingredient("Chicken"), new Ingredient("Bacon"), new Ingredient("Tomatoes")));
        recipes.add(new Recipe("Pigs in a Blanket", new Ingredient("Sausages"), new Ingredient("Bacon")));
        recipes.add(new Recipe("Tequila Shot", new Ingredient("Tequila"), new Ingredient("Lime juice")));
        recipes.add(new Recipe("Grilled Cheese", new Ingredient("Cheese"), new Ingredient("Bread")));
        return recipes;
    }
}

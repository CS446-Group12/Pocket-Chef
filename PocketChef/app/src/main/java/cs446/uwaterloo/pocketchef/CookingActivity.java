package cs446.uwaterloo.pocketchef;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import cs446.uwaterloo.pocketchef.adapters.CookingTabsAdapter;
import cs446.uwaterloo.pocketchef.adapters.IngredientUsageAdapter;
import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;
import cs446.uwaterloo.pocketchef.ui.cooking.IngredientUsageFragment;

public class CookingActivity extends AppCompatActivity {

    //public enum Rating { ONE_STAR, TWO_STARS, THREE_STARS, FOUR_STARS, FIVE_STARS }
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);
        CookingTabsAdapter cookingTabsAdapter = new CookingTabsAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(cookingTabsAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("recipe");

        TextView recipeName = findViewById(R.id.recipe_name);
        recipeName.setText(recipe.getName());
    }

    public Recipe getRecipe() {
        return recipe;
    }

    // Called when the user clicks the "Consume Ingredients" button
    public void openIngredientManager(View view) {
        //setContentView(R.layout.fragment_ingredient_usage);

        // Get list of Ingredients
        ArrayList<Ingredient> ingredients = new ArrayList<>(recipe.getAllIngredients());

        DialogFragment dialogFragment = new IngredientUsageFragment(ingredients);
        dialogFragment.show(getSupportFragmentManager(), "ingredientDialog");
    }
}
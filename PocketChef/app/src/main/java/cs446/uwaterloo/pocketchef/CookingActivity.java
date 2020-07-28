package cs446.uwaterloo.pocketchef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import cs446.uwaterloo.pocketchef.adapters.CookingTabsAdapter;
import cs446.uwaterloo.pocketchef.model.Recipe;
import cs446.uwaterloo.pocketchef.ui.cooking.CookingViewModel;

public class CookingActivity extends AppCompatActivity {

    private Recipe recipe;

    // TODO display name of the recipe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);
        final CookingTabsAdapter cookingTabsAdapter = new CookingTabsAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(cookingTabsAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("recipe");

        // accessing the ViewModel
        final CookingViewModel cookingViewModel = new ViewModelProvider(this).get(CookingViewModel.class);

        // Hook up rating
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(recipe.rating);
        cookingViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                ratingBar.setRating(recipe.rating);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                recipe.rating = rating;
                cookingViewModel.setRecipe(recipe);
            }
        });

        cookingViewModel.setRecipe(recipe);
    }


    public Recipe getRecipe() {
        return recipe;
    }
}
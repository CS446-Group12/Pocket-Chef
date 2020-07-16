package cs446.uwaterloo.pocketchef;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import cs446.uwaterloo.pocketchef.adapters.CookingTabsAdapter;
import cs446.uwaterloo.pocketchef.model.Recipe;
import cs446.uwaterloo.pocketchef.ui.cooking.CookingFragment;

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
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
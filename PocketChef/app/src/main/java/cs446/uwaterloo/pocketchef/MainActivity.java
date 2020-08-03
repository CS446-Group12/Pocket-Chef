package cs446.uwaterloo.pocketchef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cs446.uwaterloo.pocketchef.model.Recipe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_pantry, R.id.navigation_recipes)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        //Store base date for burn rate
        SharedPreferences pref = this.getSharedPreferences("Share", Context.MODE_PRIVATE);
        Date date = new Date(System.currentTimeMillis());
        if (pref.getLong("inittime", 0) == 0) {
            pref.edit().putLong("inittime", date.getTime()).apply();
        }
    }

    public void displayRecipeContents(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, CookingActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

}

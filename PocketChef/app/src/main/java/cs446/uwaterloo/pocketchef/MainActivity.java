package cs446.uwaterloo.pocketchef;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
    }

    // Uses FragmentTransactions to
    public void displayRecipeContents(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, CookingActivity.class);
        startActivity(intent);

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create new fragment and transaction
        Fragment displayRecipeFragment = new DisplayRecipeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack

        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible()) {
                    System.out.println(fragment);
                    transaction.hide(fragment);
                }
            }
        }

        //transaction.hide(fragmentManager.findFragmentById(R.id.test));
        transaction.show(displayRecipeFragment);
        //transaction.addToBackStack(null);

        // Apply a transition animation
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        // Commit the transaction
        transaction.commit();*/
    }

}

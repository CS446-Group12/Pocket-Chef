package cs446.uwaterloo.pocketchef.ui.recipes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.adapters.RecipeAdapter;
import cs446.uwaterloo.pocketchef.data.RecipeData;
import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class RecipesFragment extends Fragment {

    private RecyclerView recipeView;
    private RecipeAdapter adapter;
    private Button recipesForIngredientsButton;
    private boolean ingredientRecipesShown = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        recipeView = view.findViewById(R.id.recipe_view);

        adapter = new RecipeAdapter();
        RecipeData.manager.setAdapter(adapter);
        recipeView.setAdapter(adapter);

        Context context = getContext();
        if (context != null) {
            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            recipeView.addItemDecoration(itemDecoration);
            recipeView.setLayoutManager(new LinearLayoutManager(context));
        }

        recipesForIngredientsButton = view.findViewById(R.id.recipes_for_current_ingredients_button);
        recipesForIngredientsButton.setText("Show available recipes");
        recipesForIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ingredientRecipesShown) {
                    recipesForIngredientsButton.setText("Show available recipes");
                    ingredientRecipesShown = false;
                }
                else {
                    recipesForIngredientsButton.setText("Show all recipes");
                    ingredientRecipesShown = true;
                }
                RecipeData.manager.switchShownRecipes();
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu, menu);

        //Set the background color of the text input field of the search widget
        //Start with obtaining the search widget as an object
        MenuItem item = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) item.getActionView();
        //Now, look up the id of the text input field in the widget and obtain it
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RecipeData.manager.filterByName(newText);
                return false;
            }
        });

        int searchEditId = androidx.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        //Set the background color of the input field
        int backgroundColor = getResources().getColor(R.color.colorPrimaryDark, null);
        et.setBackgroundColor(backgroundColor);
        et.setHintTextColor(getResources().getColor(R.color.dark_gray, null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_add) {
            addRecipe();
        }
        return true;
    }

    private void addRecipe() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle("Add Recipe");

        LayoutInflater inflater = getLayoutInflater();
        View alertView = inflater.inflate(R.layout.dialog_add_recipe, null);
        alertBuilder.setView(alertView);

        final EditText editName = (EditText) alertView.findViewById(R.id.recipe_name);
        editName.setInputType(InputType.TYPE_CLASS_TEXT);

        final EditText editIngredients = (EditText) alertView.findViewById(R.id.recipe_ingredients);
        editIngredients.setInputType(InputType.TYPE_CLASS_TEXT);

        alertBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Ingredient> ingredients = new ArrayList<>();
                for (final String word : editIngredients.getText().toString().split(",")) {
                    ingredients.add(new Ingredient(word.trim()));
                }
                final String name = editName.getText().toString().trim();
                RecipeData.manager.addRecipe(new Recipe(name, ingredients));
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertBuilder.show();
    }
}

package cs446.uwaterloo.pocketchef.ui.recipes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.adapters.RecipeAdapter;
import cs446.uwaterloo.pocketchef.model.RecipeAndCounts;

public class RecipesFragment extends Fragment {

    private RecyclerView recipeView;
    private RecipeAdapter adapter;
    private ToggleButton recipesForIngredientsButton;
    private boolean ingredientRecipesShown = false;
    public ProgressBar pBar;

    private RecipesViewModel recipesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        recipeView = view.findViewById(R.id.recipe_view);

        adapter = new RecipeAdapter();
        recipeView.setAdapter(adapter);

        recipesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);
        pBar = (ProgressBar) view.findViewById(R.id.recipesPBar);
        pBar.setVisibility(View.VISIBLE);

        recipesViewModel.getAvailableRecipes().observe(getViewLifecycleOwner(), new Observer<List<RecipeAndCounts>>() {
            @Override
            public void onChanged(List<RecipeAndCounts> recipeAndCounts) {
                pBar.setVisibility(View.GONE);
                adapter.setAvailableRecipes(recipeAndCounts);
            }
        });
        recipesViewModel.getAllRecipes().observe(getViewLifecycleOwner(), new Observer<List<RecipeAndCounts>>() {
            @Override
            public void onChanged(List<RecipeAndCounts> recipeAndCounts) {
                pBar.setVisibility(View.GONE);
                adapter.setAllRecipes(recipeAndCounts);
            }
        });

        Context context = getContext();
        if (context != null) {
            RecyclerView.ItemDecoration itemDecoration =
                    new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            recipeView.addItemDecoration(itemDecoration);
            recipeView.setLayoutManager(new LinearLayoutManager(context));
        }
        recipesForIngredientsButton = view.findViewById(R.id.recipes_for_current_ingredients_button);

        recipesForIngredientsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                adapter.switchShownRecipes(recipesForIngredientsButton.isChecked());
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu, menu);

        // hide add button (can't support adding recipes)
        MenuItem addButton = menu.findItem(R.id.app_bar_add);
        addButton.setVisible(false);

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
                adapter.getFilter().filter(newText);
                return true;
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
        return true;
    }

}

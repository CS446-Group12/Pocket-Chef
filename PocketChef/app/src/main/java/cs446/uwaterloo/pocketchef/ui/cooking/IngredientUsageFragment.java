package cs446.uwaterloo.pocketchef.ui.cooking;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.adapters.IngredientUsageAdapter;
import cs446.uwaterloo.pocketchef.model.Recipe;
import cs446.uwaterloo.pocketchef.model.RecipeAndIngredients;

public class IngredientUsageFragment extends DialogFragment {

    private RecyclerView recyclerView;
    private IngredientUsageAdapter adapter;

    private final Recipe recipe;

    public IngredientUsageFragment(Recipe recipe) {
        this.recipe = recipe;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ingredient Usage");
        builder.setMessage("Update the pantry with the number of ingredients you used.");

        // Get layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_ingredient_usage, null);
        // Show progress bar
        final ProgressBar pBar = (ProgressBar) view.findViewById(R.id.pBar);
        pBar.setVisibility(View.VISIBLE);
        // Lookup RecyclerView
        recyclerView = view.findViewById(R.id.ingredient_usage_view);
        // Create adapter and pass in Ingredients
        adapter = new IngredientUsageAdapter();
        // Attach adapter to RecyclerView to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position items
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // hook up data
        final CookingViewModel cookingViewModel = new ViewModelProvider(getActivity()).get(CookingViewModel.class);

        cookingViewModel.getRecipeAndIngredients(recipe).observe(getActivity(), new Observer<RecipeAndIngredients>() {
            @Override
            public void onChanged(RecipeAndIngredients recipeAndIngredients) {
                Log.d("IngredientUsage",
                        String.format("\n\nRecipe source: (%d)%s\nRecipe result: (%d)%s\nRecipe Ingredients: %s\n",
                                recipe.ID,
                                recipe.title,
                                recipeAndIngredients.recipe.ID,
                                recipeAndIngredients.recipe.title,
                                TextUtils.join(",", recipeAndIngredients.ingredients)
                        ));
                pBar.setVisibility(View.GONE);
                adapter.setIngredients(recipeAndIngredients.ingredients);


            }
        });




        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                cookingViewModel.updateIngredients(adapter.getIngredients());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                IngredientUsageFragment.this.getDialog().cancel();
            }
        });

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }
}
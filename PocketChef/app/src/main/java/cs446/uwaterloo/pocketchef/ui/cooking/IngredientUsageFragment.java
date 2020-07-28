package cs446.uwaterloo.pocketchef.ui.cooking;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashSet;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.adapters.IngredientUsageAdapter;
import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class IngredientUsageFragment extends DialogFragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<Ingredient> ingredients;

    public IngredientUsageFragment(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ingredient Usage");
        builder.setMessage("Please select the ingredients that were used (these will be removed from your pantry).");

        // Get layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_ingredient_usage, null);

        // Lookup RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.ingredient_usage_view);
        // Create adapter and pass in Ingredients
        adapter = new IngredientUsageAdapter(ingredients);
        // Attach adapter to RecyclerView to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position items
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

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
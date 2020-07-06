package cs446.uwaterloo.pocketchef.ui.pantry;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.adapters.IngredientAdapter;
import cs446.uwaterloo.pocketchef.model.Ingredient;

public class PantryFragment extends Fragment {

    private RecyclerView pantryView;
    private Toolbar toolbar;
    private IngredientAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pantry, container, false);

        //Set up the RecyclerView to be used for displaying the list
        //Look up in the parent activity layout the recycler view that will list the ingredients
        pantryView = root.findViewById(R.id.pantry_view);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        pantryView.addItemDecoration(itemDecoration);

        //Initialize a sample ingredient list
        ArrayList<Ingredient> sampleIngredientsList;
        //Populate the sample data list
        sampleIngredientsList = Ingredient.createIngredientsList();
        adapter = new IngredientAdapter(sampleIngredientsList);
        //Attach the adapter to the RecyclerView
        pantryView.setAdapter(adapter);
        //Set layout manager to position the items
        pantryView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        setHasOptionsMenu(true);

        return root;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pantry_menu, menu);

    }

}

package cs446.uwaterloo.pocketchef.ui.pantry;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.adapters.IngredientAdapter;
import cs446.uwaterloo.pocketchef.model.Ingredient;

public class PantryFragment extends Fragment {
    private static final String TAG = "Pantry";

    private RecyclerView pantryView;
    private Toolbar toolbar;
    private IngredientAdapter adapter;

    private PantryViewModel pantryViewModel;

    private Handler handler;

    private ArrayAdapter<String> ingredientNameSuggestionAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pantry, container, false);

        Context context = getActivity();

        //Set up the RecyclerView to be used for displaying the list
        //Look up in the parent activity layout the recycler view that will list the ingredients
        pantryView = root.findViewById(R.id.pantry_view);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        pantryView.addItemDecoration(itemDecoration);


        // get an existing ViewModel or create one if they don't exist
        pantryViewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        // Show progress bar
        final ProgressBar pBar = (ProgressBar) root.findViewById(R.id.pantryPBar);
        pBar.setVisibility(View.VISIBLE);

        // observe changes in ingredients available to the user
        pantryViewModel.getAvailableIngredients().observe(getViewLifecycleOwner(), new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                pBar.setVisibility(View.GONE);
                adapter.setAvailableIngredients(ingredients);
            }
        });

        // Pair the adapter and the data source
        adapter = new IngredientAdapter(this);

        //Attach the adapter to the RecyclerView
        pantryView.setAdapter(adapter);
        //Set layout manager to position the items
        pantryView.setLayoutManager(new LinearLayoutManager(context));

        // make an adapter for adding ingredients
        ingredientNameSuggestionAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line);

        // update the data automatically for names
        pantryViewModel.getAllIngredients().observe(getViewLifecycleOwner(), new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(final List<Ingredient> ingredients) {
                Log.d(TAG, "Got " + ingredients.size() + " ingredients in total.");
                ingredientNameSuggestionAdapter.clear();
                for (Ingredient i : ingredients) {
                    ingredientNameSuggestionAdapter.add(i.name);
                }
                ingredientNameSuggestionAdapter.notifyDataSetChanged();
            }
        });

        setHasOptionsMenu(true);

        // utility for running code on UI thread (i.e. show toast)
        handler = new Handler();

        return root;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

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
                pantryViewModel.filterAvailableIngredients(newText).observe(
                        getViewLifecycleOwner(),
                        new Observer<List<Ingredient>>() {
                            @Override
                            public void onChanged(List<Ingredient> ingredients) {
                                adapter.setAvailableIngredients(ingredients);
                                Log.d(TAG, "Got " + ingredients.size() + " filtered ingredients!");
                            }
                        }
                );
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
            addIngredient();
        }
        return true;
    }

    private void addIngredient() {

        final Context context = getContext();

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle("Add ingredient");

        final View view = View.inflate(context, R.layout.add_ingredient, null);

        final AppCompatAutoCompleteTextView nameEditText = view.findViewById(R.id.add_ingredient_name);
        final EditText amountEditText = view.findViewById(R.id.add_ingredient_amount);
        final TextInputLayout amountTil = view.findViewById(R.id.add_ingredient_amount_til);
        nameEditText.setAdapter(ingredientNameSuggestionAdapter);

        alertBuilder.setView(view);

        alertBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final int selected  = nameEditText.getListSelection();
                final String name   = nameEditText.getText().toString().trim();
                final String amount = amountEditText.getText().toString().trim();

                // It should not be possible that the alert's positive button was selected when
                // either the name or amount were left as empty fields. In the case that it does
                // happen, log an error, display a toast message to the user, and do not submit.
                if (name.isEmpty() || amount.isEmpty()) {
                    Log.e(TAG, String.format(
                            "Add click detected despite no text present for name or field(s). " +
                            "Name: '%s'. Amount: '%s'", name, amount));
                    Toast.makeText(
                            getActivity(),
                            "Ingredient name and amount must be filled",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // This needs to be on a background thread since it queries the DB
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO more input validation
                        Ingredient ingredient;
                        if (selected != ListView.INVALID_POSITION) {
                            ingredient = (Ingredient) nameEditText.getAdapter().getItem(selected);
                        } else {
                            ingredient = pantryViewModel.findIngredientByName(name);
                            if (ingredient == null) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(
                                                getActivity(),
                                                "Couldn't find " + name,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                        }

                        ingredient.stock = Double.parseDouble(amount);
                        pantryViewModel.updateIngredient(ingredient);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(
                                        getActivity(),
                                        "Added " + name,
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }).start();

            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = alertBuilder.create();

        // Disable the positive button (to add the ingredient) initially unless the amount and name
        // fields are already filled. This currently should not be the case; however, in the future
        // previous states may be saved.
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(
                        !amountEditText.getText().toString().trim().isEmpty() &&
                        !nameEditText.getText().toString().trim().isEmpty());
            }
        });

        // This must be a final array to satisfy accessing within the addTextChangeListeners below.
        // The array records whether the both text fields in the alert dialog are currently filled.
        // If either is not, then the positive button of the dialog should not be active.
        final Boolean[] fieldsFilled = {false, false};

        // Add a text listener to the name text view to update whether its field is or is not
        // filled. If all fields are, then this will activate the positive alert button. If one or
        // more are not, then the positive alert button will be deactivated.
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fieldsFilled[0] = !s.toString().trim().isEmpty();
                boolean allFilled = true;
                for (final Boolean filled : fieldsFilled)
                    allFilled &= filled;
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(allFilled);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Add a text listener to the amount text view to update whether its field is or is not
        // filled. If all fields are, then this will activate the positive alert button. If one or
        // more are not, then the positive alert button will be deactivated.
        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fieldsFilled[1] = !s.toString().trim().isEmpty();
                boolean allFilled = true;
                for (final Boolean filled : fieldsFilled)
                    allFilled &= filled;
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(allFilled);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        alert.show();
    }

    public void deleteIngredient(Ingredient ingredient) {
        ingredient.stock = 0;
        pantryViewModel.updateIngredient(ingredient);
    }
}

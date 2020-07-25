package cs446.uwaterloo.pocketchef.ui.pantry;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.adapters.IngredientAdapter;
import cs446.uwaterloo.pocketchef.data.IngredientData;
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

        // Pair the adapter and the data source
        adapter = new IngredientAdapter();
        IngredientData.manager.setAdapter(adapter);

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
                IngredientData.manager.filterByName(newText);
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

        Context context = getContext();

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle("Add ingredient");

        final View view = View.inflate(context, R.layout.add_ingredient, null);

        final EditText nameEditText = view.findViewById(R.id.add_ingredient_name);
        final DatePicker datePicker = view.findViewById(R.id.add_ingredient_date);
        datePicker.setMinDate(System.currentTimeMillis());

        alertBuilder.setView(view);

        alertBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                Ingredient newIngredient = new Ingredient(nameEditText.getText().toString(), calendar.getTime());
                IngredientData.manager.addIngredients(newIngredient);
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

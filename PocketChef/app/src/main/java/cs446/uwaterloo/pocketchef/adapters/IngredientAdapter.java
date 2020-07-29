package cs446.uwaterloo.pocketchef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.ui.pantry.PantryFragment;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private PantryFragment fragment;

    private List<Ingredient> availableIngredients;

    public IngredientAdapter(PantryFragment fragment) {
        this.fragment = fragment;
        this.availableIngredients = new ArrayList<>();
    }

    // TODO implement searching on client side

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate the custom layout
        View ingredientView = inflater.inflate(R.layout.item_ingredient, parent, false);

        //Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(ingredientView);
        return viewHolder;

    }


    //Populate data into the item through holder
    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {

        // Get the data model based on position
        final Ingredient ingredient = availableIngredients.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.ingredientTextView;
        textView.setText(ingredient.name);

        ImageButton deleteButton = holder.deleteButton;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.deleteIngredient(ingredient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return availableIngredients.size();
    }

    public void setAvailableIngredients(List<Ingredient> ingredients) {
        availableIngredients = ingredients;
        notifyDataSetChanged();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ingredientTextView;
        public ImageButton deleteButton;
        public TextView ingredientDateView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
         public ViewHolder(View itemView) {

             // Stores the itemView in a public final member variable that can be used
             // to access the context from any ViewHolder instance.
             super(itemView);

             ingredientTextView = itemView.findViewById(R.id.ingredient_text);
             deleteButton = itemView.findViewById(R.id.delete_ingredient_button);

         }
    }
}

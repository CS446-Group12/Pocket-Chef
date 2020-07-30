package cs446.uwaterloo.pocketchef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.model.Ingredient;

public class IngredientUsageAdapter extends RecyclerView.Adapter<IngredientUsageAdapter.ViewHolder> {

    private static final double STEP_SIZE = 0.5;
    private List<Ingredient> ingredients;

    public IngredientUsageAdapter() {
        this.ingredients = new ArrayList<>();
    }

    // Inflates item layout and creates holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ingredientUsageView = inflater.inflate(R.layout.item_ingredient_quantity, parent, false);

        // Return a new holder instance
        return new ViewHolder(ingredientUsageView);
    }

    // Populates data into item through holder
    @Override
    public void onBindViewHolder(IngredientUsageAdapter.ViewHolder holder, final int position) {
        // Get the data model based on position
        final Ingredient ingredient = ingredients.get(position);

        // Set item views based on your views and data model
        final TextView ingredientNameView = holder.ingredientNameView;
        ingredientNameView.setText(ingredient.name);

        final EditText ingredientQuantityView = holder.ingredientQuantityView;
        ingredientQuantityView.setText(ingredient.getFormattedStock());

        Button subtractButton = holder.subtractButton;
        subtractButton.setText("-");
        subtractButton.setEnabled(true);
        subtractButton.setOnClickListener(new IngredientAmountChangeListener(
                IngredientAmountChangeListener.Mode.SUBTRACT,
                ingredientQuantityView,
                ingredient
        ));

        Button addButton = holder.addButton;
        addButton.setText("+");
        addButton.setEnabled(true);
        addButton.setOnClickListener(new IngredientAmountChangeListener(
                IngredientAmountChangeListener.Mode.ADD,
                ingredientQuantityView,
                ingredient
        ));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ingredientNameView;
        public Button subtractButton;
        public EditText ingredientQuantityView;
        public Button addButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ingredientNameView = (TextView) itemView.findViewById(R.id.ingredient_name);
            subtractButton = (Button) itemView.findViewById(R.id.subtract_button);
            ingredientQuantityView = (EditText) itemView.findViewById(R.id.ingredient_quantity);
            addButton = (Button) itemView.findViewById(R.id.add_button);
        }
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }


    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    private static class IngredientAmountChangeListener implements View.OnClickListener {

        public enum Mode {
            SUBTRACT,
            ADD
        }

        private Mode mode;
        private EditText ingredientQuantityView;
        private Ingredient ingredient;

        public IngredientAmountChangeListener(Mode mode, EditText ingredientQuantityView, Ingredient ingredient) {
            this.mode = mode;
            this.ingredientQuantityView = ingredientQuantityView;
            this.ingredient = ingredient;
        }

        @Override
        public void onClick(View view) {
            double value;
            try {
                value = Double.parseDouble(ingredientQuantityView.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            switch (mode) {
                case ADD:
                    value += STEP_SIZE;
                    break;
                case SUBTRACT:
                    if (value > STEP_SIZE) {
                        value -= STEP_SIZE;
                    }
            }

            ingredient.stock = value;

            ingredientQuantityView.setText(ingredient.getFormattedStock());
        }
    }
}

package cs446.uwaterloo.pocketchef.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cs446.uwaterloo.pocketchef.MainActivity;
import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.model.Recipe;
import cs446.uwaterloo.pocketchef.model.RecipeAndCounts;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> implements Filterable {

    private List<RecipeAndCounts> availableRecipes;
    private List<RecipeAndCounts> allRecipes;
    private List<RecipeAndCounts> filteredRecipes;

    public RecipeAdapter() {
        availableRecipes = new ArrayList<>();
        allRecipes = new ArrayList<>();
        filteredRecipes = new ArrayList<>();
        showingAllRecipes = false;
        Log.d("Recipe", "Created recipe adapter");
    }

    private boolean showingAllRecipes;

    class RecipeFilter extends Filter {
        private CharSequence lastFilter = null;

        @Override
        protected FilterResults performFiltering(CharSequence filterText) {
            lastFilter = filterText;

            List<RecipeAndCounts> filteredRecipes = new ArrayList<>();
            List<RecipeAndCounts> source = getCurrentRecipeSource();
            if (filterText == null || filterText.length() == 0) {
                filteredRecipes.addAll(source);
            } else {
                String filterPattern = filterText.toString().trim().toLowerCase();
                for (RecipeAndCounts rc : source) {
                    if (rc.recipe.title.toLowerCase().contains(filterPattern))
                        filteredRecipes.add(rc);
                }
            }

            Log.d("Recipe", "Got " + filteredRecipes.size() + " filtered recipes.");

            FilterResults results = new FilterResults();
            results.values = filteredRecipes;
            return results;
        }

        @Override
        @SuppressWarnings(value = "unchecked")
        protected void publishResults(CharSequence filterText, FilterResults filterResults) {
            filteredRecipes = (List<RecipeAndCounts>) filterResults.values;
            notifyDataSetChanged();
        }

        public void repeatLastFilter() {
            this.filter(lastFilter);
            notifyDataSetChanged();
        }
    }

    private RecipeFilter recipeFilter = new RecipeFilter();

    private List<RecipeAndCounts> getCurrentRecipeSource() {
        return showingAllRecipes ? allRecipes : availableRecipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom recipe layout
        View recipeView = inflater.inflate(R.layout.item_recipe, parent, false);

        // Create an OnClickListener for each recipe item in the RecyclerView
        // This is used to display the recipe's contents
        recipeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                RecyclerView recyclerView = (RecyclerView) v.getParent();
                int position = recyclerView.getChildLayoutPosition(v);

                Recipe recipe = filteredRecipes.get(position).recipe;

                ((MainActivity) context).displayRecipeContents(recipe);
            }
        });

        // From the inflated layout, generate a ViewHolder
        return new ViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipeAndCounts recipeAndCounts = filteredRecipes.get(position);

        TextView textView = holder.recipeTextView;
        textView.setText(recipeAndCounts.recipe.title);

        RatingBar ratingBar = holder.ratingBar;
        ratingBar.setRating(recipeAndCounts.recipe.rating);
    }

    @Override
    public int getItemCount() {
        return filteredRecipes.size();
    }

    public void switchShownRecipes(boolean showAllRecipes) {
        showingAllRecipes = showAllRecipes;
        recipeFilter.repeatLastFilter();
    }

    public boolean isShowingAllRecipes() {
        return showingAllRecipes;
    }

    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    public void setAvailableRecipes(List<RecipeAndCounts> availableRecipes) {
        this.availableRecipes = availableRecipes;
        if (!showingAllRecipes)
            recipeFilter.repeatLastFilter();
        Log.d("Recipe", "Got " + availableRecipes.size() + " available recipes.");
    }

    public void setAllRecipes(List<RecipeAndCounts> allRecipes) {
        this.allRecipes = allRecipes;
        if (showingAllRecipes)
            recipeFilter.repeatLastFilter();
        Log.d("Recipe", "Got " + availableRecipes.size() + " all recipes.");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTextView;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeTextView = itemView.findViewById(R.id.recipe_text);
            ratingBar = itemView.findViewById(R.id.recipe_rating);
        }
    }
}

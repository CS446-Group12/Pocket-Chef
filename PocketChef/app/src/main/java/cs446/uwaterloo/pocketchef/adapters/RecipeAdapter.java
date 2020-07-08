package cs446.uwaterloo.pocketchef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.data.RecipeData;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public RecipeAdapter() {}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom recipe layout
        View recipeView = inflater.inflate(R.layout.item_recipe, parent, false);

        // From the inflated layout, generate a ViewHolder
        return new ViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<Recipe> recipes = RecipeData.getCurrentRecipes();
        Recipe recipe = recipes.get(position);

        TextView textView = holder.recipeTextView;
        textView.setText(recipe.getName());

        RatingBar ratingBar = holder.ratingBar;
    }

    @Override
    public int getItemCount() { return RecipeData.getCurrentRecipes().size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTextView;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeTextView = (TextView) itemView.findViewById(R.id.recipe_text);
            ratingBar = (RatingBar) itemView.findViewById(R.id.recipe_rating);
        }
    }
}

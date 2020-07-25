package cs446.uwaterloo.pocketchef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cs446.uwaterloo.pocketchef.MainActivity;
import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.data.RecipeData;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public RecipeAdapter() {}

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
                Recipe recipe = RecipeData.manager.getDisplayRecipes().get(position);

                ((MainActivity)context).displayRecipeContents(recipe);
            }
        });

        // From the inflated layout, generate a ViewHolder
        return new ViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<Recipe> recipes = RecipeData.manager.getDisplayRecipes();
        Recipe recipe = recipes.get(position);

        TextView textView = holder.recipeTextView;
        textView.setText(recipe.getName());

        RatingBar ratingBar = holder.ratingBar;
    }

    @Override
    public int getItemCount() { return RecipeData.manager.getDisplayRecipes().size(); }

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

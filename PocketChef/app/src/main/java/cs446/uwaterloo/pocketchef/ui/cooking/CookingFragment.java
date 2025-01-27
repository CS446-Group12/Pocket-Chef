package cs446.uwaterloo.pocketchef.ui.cooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import cs446.uwaterloo.pocketchef.CookingActivity;
import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.model.Recipe;

/**
 * A placeholder fragment containing a simple view.
 */
public class CookingFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final int INGREDIENTS_INDEX = 1;
    private static final int RECIPE_INDEX = 2;

    private CookingViewModel cookingViewModel;
    private int index;

    public static CookingFragment newInstance(int index) {
        CookingFragment fragment = new CookingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        this.index = index;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cooking, container, false);
        final TextView textView = root.findViewById(R.id.section_label);

        // Get Recipe from CookingActivity
        CookingActivity cookingActivity = (CookingActivity) getActivity();
        assert cookingActivity != null;
        Recipe recipe = cookingActivity.getRecipe();

        String text;
        switch (index) {
            case INGREDIENTS_INDEX:
                text = prettyPrintIngredients(recipe);
                break;
            case RECIPE_INDEX:
                text = prettyPrintSteps(recipe);
                break;
            default:
                throw new RuntimeException("invalid page index!");
        }

        textView.setText(text);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /* Methods for outputting strings */
    private String prettyPrintIngredients(Recipe recipe) {
        String rtnStr = "";
        if (recipe != null)
            for (String line : recipe.getIngredients()) {
                rtnStr += line + "\n\n";
            }
        return rtnStr;
    }

    private String prettyPrintSteps(Recipe recipe) {
        String rtnStr = "";
        int counter = 1;
        if (recipe != null)
            for (String s : recipe.getDirections()) {
                rtnStr += "Step " + counter + ": " + s + "\n\n";
                counter += 1;
            }
        return rtnStr;
    }
}
package cs446.uwaterloo.pocketchef.ui.cooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import cs446.uwaterloo.pocketchef.CookingActivity;
import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.model.Ingredient;
import cs446.uwaterloo.pocketchef.model.Recipe;

/**
 * A placeholder fragment containing a simple view.
 */
public class CookingFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private CookingViewModel cookingViewModel;

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
        cookingViewModel = ViewModelProviders.of(this).get(CookingViewModel.class);

        // Get Recipe from CookingActivity
        CookingActivity cookingActivity = (CookingActivity) getActivity();
        Recipe recipe = cookingActivity.getRecipe();

        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        //cookingViewModel.setIndex(index);

        if (index == 1) {
            cookingViewModel.setData(prettyPrintIngredients(recipe));
        } else if (index == 2){
            cookingViewModel.setData(prettyPrintSteps(recipe));
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cooking, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        cookingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /* Methods for outputting strings */
    private String prettyPrintIngredients(Recipe recipe) {
        String rtnStr = "";
        for (Ingredient i : recipe.getAllIngredients()) {
            rtnStr += i.toString() + "\n\n";
        }
        return rtnStr;
    }

    private String prettyPrintSteps(Recipe recipe) {
        String rtnStr = "";
        int counter = 1;
        for (String s : recipe.getSteps()) {
            rtnStr += "Step " + counter + ": " + s + "\n\n";
            counter += 1;
        }
        return rtnStr;
    }
}
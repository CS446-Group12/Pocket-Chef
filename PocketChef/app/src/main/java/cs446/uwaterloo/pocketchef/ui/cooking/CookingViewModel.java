package cs446.uwaterloo.pocketchef.ui.cooking;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import cs446.uwaterloo.pocketchef.R;

public class CookingViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            //return "Hello world from section: " + input;
            if (input == 1) {
                return "Ingredients here";
            } else if (input == 2) {
                return "Recipe here";
            } else {
                return "";
            }
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}
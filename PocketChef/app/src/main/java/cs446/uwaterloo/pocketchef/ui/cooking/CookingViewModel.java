package cs446.uwaterloo.pocketchef.ui.cooking;

import android.content.Intent;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cs446.uwaterloo.pocketchef.R;
import cs446.uwaterloo.pocketchef.model.Recipe;

public class CookingViewModel extends ViewModel {

    //private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private MutableLiveData<String> mData = new MutableLiveData<>();

    private LiveData<String> mText = Transformations.map(mData, new Function<String, String>() {
        @Override
        public String apply(String input) {
            return input;
        }
    });

//    public void setIndex(int index) {
//        mIndex.setValue(index);
//    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setData(String data) {
        this.mData.setValue(data);
    }
}
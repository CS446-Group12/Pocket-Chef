package cs446.uwaterloo.pocketchef.data;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentSkipListSet;

import cs446.uwaterloo.pocketchef.model.BaseModel;

public abstract class BaseDataManager<T extends BaseModel> {
    protected RecyclerView.Adapter<?> adapter;

    protected String filterKeyword;
    protected boolean displayDataIsModified = false;
    protected ArrayList<T> displayData;
    protected ConcurrentSkipListSet<T> sortedData;

    protected BaseDataManager(Collection<T> data) {
        adapter      = null;
        sortedData   = new ConcurrentSkipListSet<>(data);
        displayData  = new ArrayList<>(sortedData);
    }

    public void setAdapter(RecyclerView.Adapter<?> newAdapter) {
        adapter = newAdapter;
    }

    protected void resetDisplayData() {
        if (displayDataIsModified) {
            displayData = new ArrayList<>(sortedData);
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }

    protected void addData(Collection<T> data) {
        displayDataIsModified = sortedData.addAll(data);
        resetDisplayData();
        if (filterKeyword != null && !filterKeyword.isEmpty())
            filterByName(filterKeyword);
    }

    protected void removeData(Collection<T> data) {
        displayDataIsModified = sortedData.removeAll(data);
        resetDisplayData();
        if (filterKeyword != null && !filterKeyword.isEmpty())
            filterByName(filterKeyword);
    }

    public void filterByName(@NonNull String keyword) {
        displayDataIsModified = true;
        resetDisplayData();

        filterKeyword = keyword;
        if (filterKeyword.isEmpty())
            return;

        LinkedList<T> prefix   = new LinkedList<>();
        LinkedList<T> contains = new LinkedList<>();

        String capsKey = keyword.toUpperCase();
        String name;
        for (final T value : displayData) {
            name = value.getName().toUpperCase();
            if (name.startsWith(capsKey))
                prefix.add(value);
            else if (name.contains(capsKey))
                contains.add(value);
        }

        displayData = new ArrayList<>(prefix.size() + contains.size());
        displayData.addAll(prefix);
        displayData.addAll(contains);

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}

package cs446.uwaterloo.pocketchef.model;

import androidx.annotation.NonNull;

public abstract class BaseModel implements Comparable<BaseModel> {
    protected String name;

    protected BaseModel(String newName) {
        name = newName;
    }

    @Override
    public int compareTo(@NonNull BaseModel o) {
        return getName().compareToIgnoreCase(o.getName());
    }

    public String getName() {
        return name;
    }
}

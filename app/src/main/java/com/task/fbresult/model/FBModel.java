package com.task.fbresult.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class FBModel implements Serializable {
    private String firebaseId;

    public FBModel() {
    }

    public FBModel(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String id) {
        this.firebaseId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FBModel fbModel = (FBModel) o;
        return Objects.equals(firebaseId, fbModel.firebaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firebaseId);
    }
}

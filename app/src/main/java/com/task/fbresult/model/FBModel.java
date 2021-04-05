package com.task.fbresult.model;

import java.io.Serializable;

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
}

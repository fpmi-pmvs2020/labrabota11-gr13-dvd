package com.task.fbresult.model;

public abstract class FBModel {
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

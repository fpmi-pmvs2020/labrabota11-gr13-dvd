package com.task.fbresult.model;

public class DutyType extends FBModel{
    private String title;

    public DutyType(String title) {
        this.title = title;
    }

    public DutyType() {
    }

    public DutyType(String id, String title) {
        super(id);
        this.title = title;
    }

    //region get/set

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    //endregion
}

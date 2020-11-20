package com.task.fbresult.model;

public class DutyTypes {
    private long id;
    private String title;

    public DutyTypes(long id, String title) {
        this.id = id;
        this.title = title;
    }

    //region get/set
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    //endregion
}

package com.task.fbresult.model;

public class Role extends FBModel{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Role() {
    }

    public Role(String id, String name) {
        super(id);
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }
}

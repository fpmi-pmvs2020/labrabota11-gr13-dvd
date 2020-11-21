package com.task.fbresult.model;

public class Role {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }
}

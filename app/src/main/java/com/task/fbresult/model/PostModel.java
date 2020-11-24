package com.task.fbresult.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostModel {
    public int rowWidth;
    public int rowHeight;
    public List<PeopleOnDuty> records;

    public PostModel(int rowWidth, int rowHeight, List<PeopleOnDuty> records) {
        this.rowWidth = rowWidth;
        this.rowHeight = rowHeight;
        this.records = records;
    }
}



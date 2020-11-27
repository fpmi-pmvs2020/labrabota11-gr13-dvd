package com.task.fbresult.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PostModel {

    public int rowWidth;
    public int rowHeight;
    public List<People> records;

    public PostModel(int rowWidth, int rowHeight, List<PeopleOnDuty> records) {
        this.rowWidth = rowWidth;
        this.rowHeight = rowHeight;
        this.records = records.stream()
                .map(e -> new People(e.getPersonId(),e.getFrom().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+"Z", e.getTo().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+"Z"))
                .collect(Collectors.toList());

    }

    class People{
        public long personId;
        public String from;
        public String to;

        public People(long personId, String from, String to) {
            this.personId = personId;
            this.from = from;
            this.to = to;
        }
    }


}



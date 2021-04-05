package com.task.fbresult.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class PeopleOnDuty extends FBModel{
    private String personId;
    private String dutyId;
    private String from;
    private String to;

    public PeopleOnDuty() {
    }

    public PeopleOnDuty(String id, String personId, String dutyId, LocalDateTime from, LocalDateTime to) {
        super(id);
        this.personId = personId;
        this.dutyId = dutyId;
        this.from = from.toString();
        this.to = to.toString();
    }

    public PeopleOnDuty(String personId, String dutyId, LocalDateTime from, LocalDateTime to) {
        this.personId = personId;
        this.dutyId = dutyId;
        this.from = from.toString();
        this.to = to.toString();
    }

    //region get/set

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime fromAsLocalDateTime(){
        return LocalDateTime.parse(from);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime toAsLocalDateTime(){
        return LocalDateTime.parse(to);
    }

    @Override
    public String toString() {
        return "PeopleOnDuty{" +
                "personId='" + personId + '\'' +
                ", dutyId='" + dutyId + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    //endregion
}

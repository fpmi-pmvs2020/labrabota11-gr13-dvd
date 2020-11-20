package com.task.fbresult.model;

import java.time.LocalDateTime;

public class Duty {

    private long id;
    private LocalDateTime from;
    private LocalDateTime to;
    private String type;
    private int maxPeople;

    public Duty(long id, LocalDateTime from, LocalDateTime to, String type, int maxPeople) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.type = type;
        this.maxPeople = maxPeople;
    }

    //region get/set
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }
    //endregion
}

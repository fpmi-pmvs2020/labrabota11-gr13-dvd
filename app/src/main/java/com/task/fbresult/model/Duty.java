package com.task.fbresult.model;

public class Duty {
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getFirstWorker() {
        return firstWorker;
    }

    String firstWorker;

    String partner;

    public Duty(String date, String partner, String firstWorker) {
        this.date = date;
        this.partner = partner;
        this.firstWorker = firstWorker;
    }
}

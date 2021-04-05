package com.task.fbresult.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Duty extends FBModel implements Serializable, Parcelable {
    private String from;
    private String to;
    private String typeId;
    private int maxPeople;

    public Duty() {
    }

    public Duty(String id, LocalDateTime from, LocalDateTime to, String type, int maxPeople) {
        super(id);
        this.from = from.toString();
        this.to = to.toString();
        this.typeId = type;
        this.maxPeople = maxPeople;
    }

    public Duty(LocalDateTime from, LocalDateTime to, String type, int maxPeople) {
        this.from = from.toString();
        this.to = to.toString();
        this.typeId = type;
        this.maxPeople = maxPeople;
    }

    //region get/set

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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
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
        return "Duty{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", typeId='" + typeId + '\'' +
                ", maxPeople=" + maxPeople +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }

    public static final Parcelable.Creator<Duty> CREATOR = new Parcelable.Creator<Duty>() {

        @Override
        public Duty createFromParcel(Parcel source) {
            return (Duty)source.readSerializable();
        }

        @Override
        public Duty[] newArray(int size) {
            return new Duty[size];
        }
    };

    //endregion
}

package com.task.fbresult.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.task.fbresult.util.LocalDateTimeInterval;

import java.io.Serializable;
import java.util.Objects;

import lombok.var;

public class DutyIntervalData implements Serializable, Parcelable {
    private String peopleOnDutyId;
    private String personId;
    private String from;
    private String to;

    public DutyIntervalData() {
    }

    public DutyIntervalData(String peopleOnDutyId, String personId, String from, String to) {
        this.peopleOnDutyId = peopleOnDutyId;
        this.personId = personId;
        this.from = from;
        this.to = to;
    }

    public static DutyIntervalData of(String recipientId) {
        return new DutyIntervalData(null, recipientId, null, null);
    }

    public static DutyIntervalData of(PeopleOnDuty peopleOnDuty, LocalDateTimeInterval localDateTimeInterval) {
        var id = peopleOnDuty.getFirebaseId();
        var personId = peopleOnDuty.getPersonId();
        var from = localDateTimeInterval.getStart().toString();
        var to = localDateTimeInterval.getEnd().toString();
        return new DutyIntervalData(id, personId, from, to);
    }

    public String getPeopleOnDutyId() {
        return peopleOnDutyId;
    }

    public void setPeopleOnDutyId(String peopleOnDutyId) {
        this.peopleOnDutyId = peopleOnDutyId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }

    public static final Parcelable.Creator<DutyIntervalData> CREATOR = new Parcelable.Creator<DutyIntervalData>() {

        @Override
        public DutyIntervalData createFromParcel(Parcel source) {
            return (DutyIntervalData) source.readSerializable();
        }

        @Override
        public DutyIntervalData[] newArray(int size) {
            return new DutyIntervalData[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DutyIntervalData that = (DutyIntervalData) o;
        return Objects.equals(peopleOnDutyId, that.peopleOnDutyId) &&
                Objects.equals(personId, that.personId) &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peopleOnDutyId, personId, from, to);
    }
}

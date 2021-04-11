package com.task.fbresult.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class MyMessage extends FBModel implements Serializable, Parcelable {
    DutyIntervalData authorIntervalData;
    DutyIntervalData recipientIntervalData;
    private MessageState messageState = MessageState.SENT;

    public MyMessage(){
        super();
    }

    public MyMessage(DutyIntervalData authorIntervalData, DutyIntervalData recipientIntervalData) {
        this.authorIntervalData = authorIntervalData;
        this.recipientIntervalData = recipientIntervalData;
    }

    public DutyIntervalData getAuthorIntervalData() {
        return authorIntervalData;
    }

    public void setAuthorIntervalData(DutyIntervalData authorIntervalData) {
        this.authorIntervalData = authorIntervalData;
    }

    public DutyIntervalData getRecipientIntervalData() {
        return recipientIntervalData;
    }

    public void setRecipientIntervalData(DutyIntervalData recipientIntervalData) {
        this.recipientIntervalData = recipientIntervalData;
    }

    public MessageState getMessageState() {
        return messageState;
    }

    public void setMessageState(MessageState messageState) {
        this.messageState = messageState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }

    public static final Parcelable.Creator<MyMessage> CREATOR = new Parcelable.Creator<MyMessage>() {

        @Override
        public MyMessage createFromParcel(Parcel source) {
            return (MyMessage) source.readSerializable();
        }

        @Override
        public MyMessage[] newArray(int size) {
            return new MyMessage[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyMessage myMessage = (MyMessage) o;
        return Objects.equals(
                authorIntervalData.getPeopleOnDutyId(),
                myMessage.getAuthorIntervalData().getPeopleOnDutyId()) &&
                Objects.equals(
                        recipientIntervalData.getPeopleOnDutyId(),
                        myMessage.getRecipientIntervalData().getPeopleOnDutyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                authorIntervalData.getPeopleOnDutyId(),
                recipientIntervalData.getPeopleOnDutyId()
        );
    }
}

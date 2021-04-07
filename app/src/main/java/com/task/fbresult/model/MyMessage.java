package com.task.fbresult.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.task.fbresult.util.LocalDateTimeInterval;

import java.io.Serializable;
import java.util.Objects;

public class MyMessage extends FBModel implements Serializable, Parcelable {
    private String authorOnDutyId;
    private String recipientOnDutyId;
    private String authorId;
    private String recipientId;
    private MessageState messageState = MessageState.SENT;
    private String myDutyFrom;
    private String myDutyTo;

    private String otherDutyFrom;
    private String otherDutyTo;

    public String getMyDutyFrom() {
        return myDutyFrom;
    }

    public void setMyDutyFrom(String myDutyFrom) {
        this.myDutyFrom = myDutyFrom;
    }

    public String getMyDutyTo() {
        return myDutyTo;
    }

    public void setMyDutyTo(String myDutyTo) {
        this.myDutyTo = myDutyTo;
    }

    public String getOtherDutyFrom() {
        return otherDutyFrom;
    }

    public void setOtherDutyFrom(String otherDutyFrom) {
        this.otherDutyFrom = otherDutyFrom;
    }

    public String getOtherDutyTo() {
        return otherDutyTo;
    }

    public void setOtherDutyTo(String otherDutyTo) {
        this.otherDutyTo = otherDutyTo;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public MessageState getMessageState() {
        return messageState;
    }

    public void setMessageState(MessageState messageState) {
        this.messageState = messageState;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MyMessage(PeopleOnDuty authorOnDuty, PeopleOnDuty recipientOnDuty,
                     LocalDateTimeInterval myDutyInterval,LocalDateTimeInterval otherDutyInterval) {
        this.authorOnDutyId = authorOnDuty.getFirebaseId();
        this.recipientOnDutyId = recipientOnDuty.getFirebaseId();
        this.authorId = authorOnDuty.getPersonId();
        this.recipientId = recipientOnDuty.getFirebaseId();
        this.myDutyFrom = myDutyInterval.getStart().toString();
        this.myDutyTo = myDutyInterval.getEnd().toString();
        this.otherDutyFrom = otherDutyInterval.getStart().toString();
        this.otherDutyTo = otherDutyInterval.getEnd().toString();
    }

    public MyMessage() {
    }

    public String getAuthorOnDutyId() {
        return authorOnDutyId;
    }

    public void setAuthorOnDutyId(String authorOnDutyId) {
        this.authorOnDutyId = authorOnDutyId;
    }

    public String getRecipientOnDutyId() {
        return recipientOnDutyId;
    }

    public void setRecipientOnDutyId(String recipientOnDutyId) {
        this.recipientOnDutyId = recipientOnDutyId;
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
        return Objects.equals(authorOnDutyId, myMessage.authorOnDutyId) &&
                Objects.equals(recipientOnDutyId, myMessage.recipientOnDutyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), authorOnDutyId, recipientOnDutyId);
    }
}

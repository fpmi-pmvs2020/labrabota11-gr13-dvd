package com.task.fbresult.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lombok.Builder;

enum MessageState {
    SENT,READ,ACCEPTED,DECLINED
};

public class MyMessage extends FBModel implements Serializable, Parcelable {
    private String authorId;
    private String recipientId;

    private String dutyId;

    private String from;
    private String to;

    public MessageState getMessageState() {
        return messageState;
    }

    public void setMessageState(MessageState messageState) {
        this.messageState = messageState;
    }

    private MessageState messageState = MessageState.SENT;


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

    public MyMessage(String authorId, String recipientId, String dutyId, String from, String to) {
        this.authorId = authorId;
        this.recipientId = recipientId;
        this.dutyId = dutyId;
        this.from = from;
        this.to = to;
    }

    public MyMessage(String firebaseId, String authorId, String recipientId, String dutyId, String from, String to) {
        super(firebaseId);
        this.authorId = authorId;
        this.recipientId = recipientId;
        this.dutyId = dutyId;
        this.from = from;
        this.to = to;
    }

    public MyMessage(String authorId, String recipientId, String dutyId) {
        this.authorId = authorId;
        this.recipientId = recipientId;
        this.dutyId = dutyId;
    }

    public MyMessage(String firebaseId, String authorId, String recipientId, String dutyId) {
        super(firebaseId);
        this.authorId = authorId;
        this.recipientId = recipientId;
        this.dutyId = dutyId;
    }

    public MyMessage() {
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

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
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
}

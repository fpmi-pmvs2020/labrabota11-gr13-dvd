package com.task.fbresult.model;

import java.time.LocalDateTime;

public class PeopleOnDuty {
    private long id;
    private long personId;
    private long dutyId;
    private LocalDateTime from;
    private LocalDateTime to;

    public PeopleOnDuty(int id, int personId, int dutyId, LocalDateTime from, LocalDateTime to) {
        this.id = id;
        this.personId = personId;
        this.dutyId = dutyId;
        this.from = from;
        this.to = to;
    }


    //region get/set
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getDutyId() {
        return dutyId;
    }

    public void setDutyId(long dutyId) {
        this.dutyId = dutyId;
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
    //endregion
}

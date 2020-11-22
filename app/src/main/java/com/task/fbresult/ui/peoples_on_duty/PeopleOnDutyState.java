package com.task.fbresult.ui.peoples_on_duty;

public enum PeopleOnDutyState {
    ME(0),
    ENDED(4),
    IN_PROGRESS(2),
    IN_FUTURE(3);

    public int order;

    PeopleOnDutyState(int i) {
        order = i;
    }
}

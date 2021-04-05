package com.task.fbresult.db.fbdao;

public class ConstraintPair {
    private String paramValue;
    private ConstraintType constraintType;

    public ConstraintPair(String paramValue, ConstraintType constraintType) {
        this.paramValue = paramValue;
        this.constraintType = constraintType;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(ConstraintType constraintType) {
        this.constraintType = constraintType;
    }
}

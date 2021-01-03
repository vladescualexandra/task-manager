package com.company.common.data;

public enum Status {

    NOT_STARTED ("NOT STARTED"),
    IN_PROGRESS ("IN PROGRESS"),
    DONE ("DONE");

    private String status;

    Status(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return status;
    }
}

package com.company.database.data;

public enum Status {

    NOT_STARTED ("NOT STARTED"),
    IN_PROGRESS ("IN PROGRESS"),
    ACTIVE ("ACTIVE"),
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

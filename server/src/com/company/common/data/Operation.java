package com.company.common.data;

public enum Operation {

    INSERT ("INSERT"),
    UPDATE ("UPDATE"),
    DELETE ("DELETE");

<<<<<<< HEAD
    private String operation;
=======

    private final String operation;
>>>>>>> master

    Operation(String operation) {
        this.operation = operation;
    }


    @Override
    public String toString() {
        return operation;
    }
}

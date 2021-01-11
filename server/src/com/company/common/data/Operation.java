package com.company.common.data;

public enum Operation {

    INSERT ("INSERT"),
    UPDATE ("UPDATE"),
    DELETE ("DELETE");


    private final String operation;

    Operation(String operation) {
        this.operation = operation;
    }


    @Override
    public String toString() {
        return operation;
    }
}

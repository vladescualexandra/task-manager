package com.company.common.data;

public enum Severity {

    TRIVIAL ("TRIVIAL"),
    IMPORTANT ("IMPORTANT"),
    URGENT ("URGENT");


    private String severity;

    Severity(String severity) {
        this.severity = severity;
    }


    @Override
    public String toString() {
        return severity;
    }
}

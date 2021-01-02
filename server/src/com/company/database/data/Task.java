package com.company.database.data;

import java.sql.Date;

public class Task {

    private String id;
    private String summary;
    private String description;
    private Severity severity;
    private Status status;
    private String project;


    public Task(String id) {
        this.id = id;
    }

    public Task() {

    }

    public Task(String id,
                String summary,
                String description,
                Severity severity,
                Status status,
                String project) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.project = project;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

}

package com.company.common.data;

import java.io.Serializable;

public class Task implements Serializable {

    private String id;
    private String summary;
    private String description;
    private Severity severity;
    private Status status;


    public Task() {

    }

    public Task(String id,
                String summary,
                String description,
                Severity severity,
                Status status) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.severity = severity;
        this.status = status;
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

    public void setSeverity(String severity) {
        switch (severity) {
            case "URGENT" -> this.setSeverity(Severity.URGENT);
            case "IMPORTANT" -> this.setSeverity(Severity.IMPORTANT);
            default -> this.setSeverity(Severity.TRIVIAL);
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {

        switch (status) {
            case "IN PROGRESS" -> this.setStatus(Status.IN_PROGRESS);
            case "DONE" -> this.setStatus(Status.DONE);
            default -> this.setStatus(Status.NOT_STARTED);
        }
    }


    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", severity=" + severity +
                ", status=" + status +
                '}';
    }
}

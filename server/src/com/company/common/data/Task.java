package com.company.common.data;

import java.io.Serializable;

public class Task implements Serializable {

    private int id;
    private String summary;
    private String description;
    private Severity severity;
    private Status status;


    public Task() {

    }

    public Task(
                String summary,
                String description,
                Severity severity,
                Status status) {
        this.summary = summary;
        this.description = description;
        this.severity = severity;
        this.status = status;
    }

    public Task(int id,
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static Task convertToTask(String message) {
        Task task = new Task();
        if (message != null && !message.isEmpty()) {

            int startIndexOfId = "Task{id='".length();
            int endIndexOfId = message.indexOf("', summary='");

            int id = Integer.parseInt(message.substring(startIndexOfId, endIndexOfId));

            int startIndexOfSummary = endIndexOfId + "', summary='".length();
            int endIndexOfSummary = message.indexOf("', description='");

            String summary = message.substring(startIndexOfSummary, endIndexOfSummary);

            int startIndexOfDescription = endIndexOfSummary + "', description='".length();
            int endIndexOfDescription = message.indexOf("', severity=");

            String description = message.substring(startIndexOfDescription, endIndexOfDescription);

            int startIndexOfSeverity = endIndexOfDescription + "', severity=".length();
            int endIndexOfSeverity = message.indexOf(", status=");

            String severity = message.substring(startIndexOfSeverity, endIndexOfSeverity);

            int startIndexOfStatus = endIndexOfSeverity + ", status=".length();
            int endIndexOfStatus = message.indexOf("}");

            String status = message.substring(startIndexOfStatus, endIndexOfStatus);

            task.setId(id);
            task.setSummary(summary);
            task.setDescription(description);
            task.setSeverity(severity);
            task.setStatus(status);

        }
        return task;

    }
}

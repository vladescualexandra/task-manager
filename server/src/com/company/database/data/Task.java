package data;


import java.sql.Date;

public class Task {

    private String id;
    private String summary;
    private String description;
    private Severity severity;
    private Status status;
    private String project;
    private Username assigned_to;
    private Date created;
    private Date closed;
    private Username created_by;

    public Task(String id) {
        this.id = id;
    }


    public Task(String id, String summary,
                String description, Severity severity,
                Status status, String project, Username assigned_to,
                Date created, Date closed, Username created_by) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.project = project;
        this.assigned_to = assigned_to;
        this.created = created;
        this.closed = closed;
        this.created_by = created_by;
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

    public Username getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(Username assigned_to) {
        this.assigned_to = assigned_to;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getClosed() {
        return closed;
    }

    public void setClosed(Date closed) {
        this.closed = closed;
    }

    public Username getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Username created_by) {
        this.created_by = created_by;
    }
}

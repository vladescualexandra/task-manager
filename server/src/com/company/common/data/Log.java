package com.company.common.data;

import java.io.Serializable;

public class Log implements Serializable {

    private int id;
    private int task_id;
    private Operation operation;

    public Log() {
    }

    public Log(int task_id, Operation operation) {
        this.task_id = task_id;
        this.operation = operation;
    }

    public Log(int id, int task_id, Operation operation) {
        this.id = id;
        this.task_id = task_id;
        this.operation = operation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", task_id=" + task_id +
                ", operation=" + operation.toString() +
                '}';
    }
}

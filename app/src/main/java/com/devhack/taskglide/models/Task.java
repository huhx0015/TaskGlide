package com.devhack.taskglide.models;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class Task {

    private boolean isFinished;
    private String taskDescription;

    public Task(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDescription() {
        return taskDescription;
    }
}

package com.devhack.taskglide.models;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class Task {

    private boolean status;
    private String amount;
    private String name;

    public Task(String taskName, String taskAmount, boolean status) {
        this.name = taskName;
        this.amount = taskAmount;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

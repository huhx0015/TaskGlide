package com.devhack.taskglide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class Task {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("name")
    @Expose
    private String name;

    public Task(String taskName, String taskAmount, int status) {
        this.name = taskName;
        this.amount = taskAmount;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

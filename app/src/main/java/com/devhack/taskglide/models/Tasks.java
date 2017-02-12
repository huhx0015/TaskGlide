package com.devhack.taskglide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Michael Yoon Huh on 2/12/2017.
 */

public class Tasks {

    @SerializedName("tasks")
    @Expose
    List<Task> tasks;
    @SerializedName("type")
    @Expose
    int type;

    public Tasks(List<Task> taskList, int type) {
        this.tasks = taskList;
        this.type = type;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

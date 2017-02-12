package com.devhack.taskglide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by Michael Yoon Huh on 2/12/2017.
 */

public class Message extends JSONObject {

    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("message")
    @Expose
    private String message;

    public Message(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

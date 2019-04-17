package com.example.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReadInfo {
    @Expose
    @SerializedName("uuid")
    private String uuid;

    @Expose
    @SerializedName("username")
    private String username;

    public ReadInfo(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

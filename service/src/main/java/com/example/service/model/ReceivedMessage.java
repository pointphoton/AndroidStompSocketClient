package com.example.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceivedMessage {

    @SerializedName("isSuccess")
    @Expose
    private boolean isSuccess;
    @SerializedName("errorCode")
    @Expose
    private int errorCode;
    @SerializedName("errorDescription")
    @Expose
    private String errorDescription;
    @SerializedName("messageUuid")
    @Expose
    private String messageUuid;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("messageType")
    @Expose
    private String messageType;
    @SerializedName("sourceusername")
    @Expose
    private String sourceusername;
    @SerializedName("date")
    @Expose
    private String date;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(String messageUuid) {
        this.messageUuid = messageUuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSourceusername() {
        return sourceusername;
    }

    public void setSourceusername(String sourceusername) {
        this.sourceusername = sourceusername;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

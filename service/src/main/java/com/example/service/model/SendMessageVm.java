package com.example.service.model;

import java.util.UUID;

public class SendMessageVm {


    String targetUsername;

    String message;

    String messageUuid;

    String testSessionId;

    public SendMessageVm(){}

    public SendMessageVm(String message, String targetUsername){
        this.message=message;
        this.targetUsername=targetUsername;
        this.messageUuid= UUID.randomUUID().toString();
    }

    public SendMessageVm(String message, String targetUsername,String testSessionId){
        this.message=message;
        this.targetUsername=targetUsername;
        this.testSessionId=testSessionId;
        this.messageUuid= UUID.randomUUID().toString();
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(String messageUuid) {
        this.messageUuid = messageUuid;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SendMessageVm{");
        sb.append("targetUsername='").append(targetUsername).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", messageUuid='").append(messageUuid).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

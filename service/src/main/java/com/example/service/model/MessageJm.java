package com.example.service.model;

public class MessageJm {

    private String destinationJanglerThumbnailUrl;
    private String destinationUserUuid;
    private String destinationUsername;
    private String documentThumbnailUrl;
    private String documentUuid;
    private String id;
    private String insertDate;
    private String isDestinationFollowSource;
    private String isFollowing;
    private String isSourceFollowDestination;
    private String message;
    private String messageType;
    private String notificationType;
    private String point;
    private String readStatus;
    private String sendStatus;
    private String sourceJanglerFullName;
    private String sourceJanglerThumbnailUrl;
    private String sourceUserUuid;
    private String sourceUsername;

    public MessageJm(String insertDate, String message, String messageType, String sourceJanglerThumbnailUrl, String sourceUserUuid,
                     String sourceUsername) {
        this.insertDate = insertDate;
        this.message = message;
        this.messageType = messageType;
        this.sourceJanglerThumbnailUrl = sourceJanglerThumbnailUrl;
        this.sourceUserUuid = sourceUserUuid;
        this.sourceUsername = sourceUsername;
    }

    public MessageJm(String destinationJanglerThumbnailUrl, String destinationUserUuid, String destinationUsername, String documentThumbnailUrl,
                     String documentUuid, String id, String insertDate, String isDestinationFollowSource, String isFollowing,
                     String isSourceFollowDestination, String message, String messageType, String notificationType, String point, String readStatus
            , String sendStatus, String sourceJanglerFullName, String sourceJanglerThumbnailUrl, String sourceUserUuid, String sourceUsername) {
        this.destinationJanglerThumbnailUrl = destinationJanglerThumbnailUrl;
        this.destinationUserUuid = destinationUserUuid;
        this.destinationUsername = destinationUsername;
        this.documentThumbnailUrl = documentThumbnailUrl;
        this.documentUuid = documentUuid;
        this.id = id;
        this.insertDate = insertDate;
        this.isDestinationFollowSource = isDestinationFollowSource;
        this.isFollowing = isFollowing;
        this.isSourceFollowDestination = isSourceFollowDestination;
        this.message = message;
        this.messageType = messageType;
        this.notificationType = notificationType;
        this.point = point;
        this.readStatus = readStatus;
        this.sendStatus = sendStatus;
        this.sourceJanglerFullName = sourceJanglerFullName;
        this.sourceJanglerThumbnailUrl = sourceJanglerThumbnailUrl;
        this.sourceUserUuid = sourceUserUuid;
        this.sourceUsername = sourceUsername;
    }

    public String getDestinationJanglerThumbnailUrl() {
        return destinationJanglerThumbnailUrl;
    }

    public void setDestinationJanglerThumbnailUrl(String destinationJanglerThumbnailUrl) {
        this.destinationJanglerThumbnailUrl = destinationJanglerThumbnailUrl;
    }

    public String getDestinationUserUuid() {
        return destinationUserUuid;
    }

    public void setDestinationUserUuid(String destinationUserUuid) {
        this.destinationUserUuid = destinationUserUuid;
    }

    public String getDestinationUsername() {
        return destinationUsername;
    }

    public void setDestinationUsername(String destinationUsername) {
        this.destinationUsername = destinationUsername;
    }

    public String getDocumentThumbnailUrl() {
        return documentThumbnailUrl;
    }

    public void setDocumentThumbnailUrl(String documentThumbnailUrl) {
        this.documentThumbnailUrl = documentThumbnailUrl;
    }

    public String getDocumentUuid() {
        return documentUuid;
    }

    public void setDocumentUuid(String documentUuid) {
        this.documentUuid = documentUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getIsDestinationFollowSource() {
        return isDestinationFollowSource;
    }

    public void setIsDestinationFollowSource(String isDestinationFollowSource) {
        this.isDestinationFollowSource = isDestinationFollowSource;
    }

    public String getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(String isFollowing) {
        this.isFollowing = isFollowing;
    }

    public String getIsSourceFollowDestination() {
        return isSourceFollowDestination;
    }

    public void setIsSourceFollowDestination(String isSourceFollowDestination) {
        this.isSourceFollowDestination = isSourceFollowDestination;
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

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSourceJanglerFullName() {
        return sourceJanglerFullName;
    }

    public void setSourceJanglerFullName(String sourceJanglerFullName) {
        this.sourceJanglerFullName = sourceJanglerFullName;
    }

    public String getSourceJanglerThumbnailUrl() {
        return sourceJanglerThumbnailUrl;
    }

    public void setSourceJanglerThumbnailUrl(String sourceJanglerThumbnailUrl) {
        this.sourceJanglerThumbnailUrl = sourceJanglerThumbnailUrl;
    }

    public String getSourceUserUuid() {
        return sourceUserUuid;
    }

    public void setSourceUserUuid(String sourceUserUuid) {
        this.sourceUserUuid = sourceUserUuid;
    }

    public String getSourceUsername() {
        return sourceUsername;
    }

    public void setSourceUsername(String sourceUsername) {
        this.sourceUsername = sourceUsername;
    }
}

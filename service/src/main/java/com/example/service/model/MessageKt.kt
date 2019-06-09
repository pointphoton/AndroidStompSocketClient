package com.example.service.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageKt {

    /*



        @Expose var point: Int,
        @Expose var readStatus: String,
        @Expose var sendStatus: String,
        @Expose var sourceJanglerFullName: String?,
        @Expose var sourceJanglerThumbnailUrl: String,
        @Expose var sourceUserUuid: String,
        @Expose var sourceUsername: String
    * */

    @Expose var destinationJanglerThumbnailUrl: String
    @Expose var destinationUserUuid: String
    @Expose  var destinationUsername: String
    @Expose  var documentThumbnailUrl: String?
    @Expose  var documentUuid: String?
    @Expose var id: String
    @Expose @SerializedName("inserDate") var insertDate: String
    @Expose var isDestinationFollowSource: Boolean
    @Expose  var isFollowing: Boolean
    @Expose  var isSourceFollowDestination:Boolean
    @Expose var message: String
    @Expose var messageType: String
    @Expose @Transient var notificationType: String?=null
    @Expose @Transient var point: Int?=null
    @Expose var readStatus: String
    @Expose var sendStatus: String
    @Expose var sourceJanglerFullName: String?
    @Expose var sourceJanglerThumbnailUrl: String
    @Expose var sourceUserUuid: String
    @Expose var sourceUsername: String

    constructor(insertDate: String, message: String, messageType: String, sourceJanglerThumbnailUrl: String, sourceUserUuid: String,
                sourceUsername: String) {
        this.destinationJanglerThumbnailUrl = "";
        this.destinationUserUuid = ""
        this.destinationUsername = ""
        this.documentThumbnailUrl = null
        this.documentUuid = null
        this.id = ""
        this.isDestinationFollowSource = false
        this.isFollowing = false
        this.isSourceFollowDestination=false
        this.insertDate = insertDate
        this.message = message
        this.messageType = messageType
        this.readStatus= ""
        this.sendStatus= ""
        this.sourceJanglerFullName=null


        this.sourceJanglerThumbnailUrl = sourceJanglerThumbnailUrl
        this.sourceUserUuid = sourceUserUuid
        this.sourceUsername = sourceUsername
    }

    constructor(destinationJanglerThumbnailUrl: String, destinationUserUuid: String, destinationUsername: String, documentThumbnailUrl: String,
                documentUuid: String, id: String, insertDate: String, isDestinationFollowSource: Boolean, isFollowing: Boolean,
                isSourceFollowDestination: Boolean, message: String, messageType: String,  readStatus: String,
                sendStatus: String, sourceJanglerFullName: String, sourceJanglerThumbnailUrl: String, sourceUserUuid: String, sourceUsername: String) {
        this.destinationJanglerThumbnailUrl = destinationJanglerThumbnailUrl
        this.destinationUserUuid = destinationUserUuid
        this.destinationUsername = destinationUsername
        this.documentThumbnailUrl = documentThumbnailUrl
        this.documentUuid = documentUuid
        this.id = id
        this.insertDate = insertDate
        this.isDestinationFollowSource = isDestinationFollowSource
        this.isFollowing = isFollowing
        this.isSourceFollowDestination = isSourceFollowDestination
        this.message = message
        this.messageType = messageType
        this.readStatus = readStatus
        this.sendStatus = sendStatus
        this.sourceJanglerFullName = sourceJanglerFullName
        this.sourceJanglerThumbnailUrl = sourceJanglerThumbnailUrl
        this.sourceUserUuid = sourceUserUuid
        this.sourceUsername = sourceUsername
    }
}

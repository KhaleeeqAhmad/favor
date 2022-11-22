package com.fyp.favorproject.model

class Message {
    var messageId: String? = null
    var message: String? = null
    var imageUrl: String? = null
    var timeStamp: Long = 0
    var senderId: String? = null

    constructor(){}
    constructor(
        message: String?,
        senderId: String?,
        timeStamp: Long = 0
    ){
        this.message = message
        this.senderId = senderId
        this.timeStamp = timeStamp
    }


}
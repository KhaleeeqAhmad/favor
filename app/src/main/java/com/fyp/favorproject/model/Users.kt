package com.fyp.favorproject.model


class Users{
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var profilePic: String? = null
    var uid: String? = null
    var lastMessage: String? = null


    //empty constructor
    constructor(){}

    //signup
    constructor(name: String?, email: String?, password: String?){
        this.name = name
        this.email = email
        this.password = password
    }

    //chat
    constructor(name: String?, email: String?, password: String?, profilePic: String?, uid: String?, lastMessage: String?){
        this.name = name
        this.email = email
        this.password = password
        this.profilePic = profilePic
        this.uid= uid
        this.lastMessage = lastMessage
    }

    //message
    constructor(profilePic: String?, name: String?, uid: String?, lastMessage: String?){
        this.name = name
        this.profilePic = profilePic
        this.uid= uid
        this.lastMessage = lastMessage
    }

}
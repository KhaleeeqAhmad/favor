package com.fyp.favorproject.model

data class RecentPostModel (
    val viewType: Int,
    var postID: String = "",
    var postImage: String? = null,
    var postedBy: String? = null,
    val postDescription: String,
    var postTime: Long? = null,
    var itemPrice: String? = null,
    var postLikes: Int = 0

    )
package com.fyp.favorproject.model

data class Post(
    var postID: String? = null,
    var postImage: String? = null,
    var postedBy: String? = null,
    var postDescription: String? = null,
    var postTime: Long? = null,
    var itemPrice: String? = null,
    var postLikes: Int? = null
)
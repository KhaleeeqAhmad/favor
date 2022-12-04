package com.fyp.favorproject.data

import com.fyp.favorproject.R
import com.fyp.favorproject.model.NotificationModel
import com.fyp.favorproject.model.Post
import com.fyp.favorproject.model.RecentPostModel
import kotlin.collections.listOf as listOf1

class DataSource {



    fun loadNotification() : List<NotificationModel> {
        return listOf1<NotificationModel>(
        NotificationModel(
            R.drawable.profile,
            "Rehan Share's your post",
            "just Now"
        ),
            NotificationModel(
                R.drawable.profile,
                "Rehan Share's your post",
                "just Now"
            ),
            NotificationModel(
                R.drawable.profile,
                "Rehan Share's your post",
                "just Now"
            ),
            NotificationModel(
                R.drawable.profile,
                "Rehan Share's your post",
                "just Now"
            ),
            NotificationModel(
                R.drawable.profile,
                "Rehan Share's your post",
                "just Now"
            ),
            NotificationModel(
                R.drawable.profile,
                "Rehan Share's your post",
                "just Now"
            ),


            )
    }

    fun loadPosts(): List<Post> {
        return listOf1<Post>(
        )
    }

    fun loadRecentPosts(): List<RecentPostModel> {
        return listOf1<RecentPostModel>(
            RecentPostModel(
                R.drawable.image1,
                "I need past paper of object oriented programming by Sir Usman Ali"
                ),
            RecentPostModel(
                R.drawable.image2,
                "I need past paper of object oriented programming by Sir Usman Ali"
            ),
        )
    }



}

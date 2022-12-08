package com.fyp.favorproject.model

data class Notification(
   var notificationBy: String? = null,
   var notificationTime: Long = 0,
   var notificationType: String? = null,
   var postID: String? = null,
   var notificationID: String? = null,
   var postedBy: String? = null,
   val checkNotificationStatus: Boolean? = null
)
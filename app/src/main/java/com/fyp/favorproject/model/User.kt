package com.fyp.favorproject.model


data class User(
   val name: String? = null,
   val department: String? = null,
   val regNumber: String? = null,
   val email: String? = null,
   val password: String? = null,
   var userID: String? = null,
   val userProfilePhoto: String? = null,
   val userCoverPhoto: String? = null
)
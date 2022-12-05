package com.fyp.favorproject.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Posts(
  @DrawableRes  val profileImageResourceID: Int,
  @StringRes  val userNameResourceID: Int,
  @StringRes  val userLevelResourceID: Int,
  @StringRes  val postDateResourceID: Int,
  @StringRes  val postDescriptionResourceID: Int,
  @DrawableRes  val postImageResourceID: Int,

//  // temp---solve it later
  val uid:String?=""

    )
package com.example.twitter.domain.model

data class AuthenticatedUser(
    val verificationId: String = "",
    val uid: String = "",
    val name: String = "",
    val about: String = "",
    val link: String = "",
    val phoneNumber: String = "",
    val profilePictureUrl: String? = null
)

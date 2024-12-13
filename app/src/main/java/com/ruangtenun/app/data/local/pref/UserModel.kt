package com.ruangtenun.app.data.local.pref

data class UserModel(
    val id: Int,
    val name: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)

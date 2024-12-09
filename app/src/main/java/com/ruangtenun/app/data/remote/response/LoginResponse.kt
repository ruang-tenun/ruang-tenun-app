package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("payload")
	val payload: Payload,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("token")
	val token: String
)

data class Payload(

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)

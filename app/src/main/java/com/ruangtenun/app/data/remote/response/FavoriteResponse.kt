package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class FavoriteResponse(

	@field:SerializedName("payload")
	val payload: List<PayloadItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class PayloadItem(

	@field:SerializedName("favorited_at")
	val favoritedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("product_id")
	val productId: Int,

	@field:SerializedName("favorite_id")
	val favoriteId: Int,

	@field:SerializedName("product_name")
	val productName: String
)

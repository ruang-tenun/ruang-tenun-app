package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class FavoriteResponse(

    @field:SerializedName("payload")
    val payload: List<FavoriteItem>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class FavoriteItem(
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
    val productNam: String
)

data class AddFavoriteResponse(

    @field:SerializedName("payload")
    val favorite: Favorite? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class Favorite(

    @field:SerializedName("favorited_at")
    val favoritedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("product_id")
    val productId: Int? = null
)

data class DeleteFavoriteResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddProductResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ListProductResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListProductItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ListProductItem(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("webUrl")
    val webUrl: String? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("lat")
    val lat: Double? = null,
)

data class Product(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("categories")
    val categories: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("webUrl")
    val webUrl: String? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("lat")
    val lat: Double? = null
)

data class ProductResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("story")
    val product: Product? = null
)
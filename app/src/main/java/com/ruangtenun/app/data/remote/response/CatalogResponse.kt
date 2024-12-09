package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListCatalogResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListCatalogItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ListCatalogItem(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("local")
    val local: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,
)

data class Catalog(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("local")
    val local: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,
)

data class CatalogResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("story")
    val product: Product? = null
)
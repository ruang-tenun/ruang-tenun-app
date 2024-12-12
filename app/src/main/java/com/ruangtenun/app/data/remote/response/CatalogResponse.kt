package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class CatalogResponse(

    @field:SerializedName("payload")
    val catalogItem: List<CatalogItem>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class CatalogItem(

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("category_id")
    val categoryId: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("image_url")
    val imageUrl: String? = null,
)

data class CatalogDetailResponse(

    @field:SerializedName("payload")
    val catalogDetail: List<CatalogDetail>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class CatalogDetail(

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("category_id")
    val categoryId: Int? = null,

    @field:SerializedName("image_url")
    val imageUrl: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null
)

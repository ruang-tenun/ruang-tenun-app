package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("payload")
	val payload: List<PayloadItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PayloadItem(

	@field:SerializedName("seller")
	val seller: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null,

	@field:SerializedName("ecommerce")
	val ecommerce: String? = null,

	@field:SerializedName("ecommerce_url")
	val ecommerceUrl: String? = null
)

data class AddProductResponse(

	@field:SerializedName("status")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

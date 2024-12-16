package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddProductsResponse(

	@field:SerializedName("payload")
	val payload: PayloadProduct? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PayloadProduct(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("seller_id")
	val sellerId: Int? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)

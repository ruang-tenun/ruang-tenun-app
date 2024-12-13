package com.ruangtenun.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProductsResponse(

	@field:SerializedName("payload")
	val productsItem: List<ProductsItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ProductsItem(

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
	val longitude: Double? = null
)

data class ProductDetailResponse(

	@field:SerializedName("payload")
	val detailProduct: List<ProductDetail>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ProductDetail(

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

	@field:SerializedName("ecommerce")
	val ecommerce: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("ecommerce_url")
	val ecommerceUrl: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)

data class AddProductResponse(

	@field:SerializedName("status")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
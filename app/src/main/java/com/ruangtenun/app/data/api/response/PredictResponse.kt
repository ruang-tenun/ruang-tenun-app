package com.ruangtenun.app.data.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictResponse(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("confidence_score")
	val confidenceScore: Double,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: String
): Parcelable

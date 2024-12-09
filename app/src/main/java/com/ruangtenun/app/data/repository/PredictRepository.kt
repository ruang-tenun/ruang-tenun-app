package com.ruangtenun.app.data.repository

import com.ruangtenun.app.data.api.response.PredictResponse
import com.ruangtenun.app.data.api.retrofit.ApiServicePredict
import okhttp3.MultipartBody

class PredictRepository private constructor(
    private val apiServicePredict: ApiServicePredict
) {
    suspend fun predict(image: MultipartBody.Part) : PredictResponse {
        return apiServicePredict.predict(image)
    }

    companion object {
        @Volatile
        private var instance: PredictRepository? = null
        fun getInstance(
            apiServicePredict: ApiServicePredict
        ): PredictRepository = instance ?: synchronized(this) {
            instance ?: PredictRepository(apiServicePredict).also { instance = it }
        }
    }
}
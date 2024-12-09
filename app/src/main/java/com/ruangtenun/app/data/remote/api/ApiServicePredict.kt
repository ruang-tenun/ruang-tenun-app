package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.data.remote.response.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServicePredict {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): FileUploadResponse
}
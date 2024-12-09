package com.ruangtenun.app.data.api.retrofit

import com.ruangtenun.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private const val BASE_URL_AUTH = BuildConfig.BASE_URL_AUTH
    private const val BASE_URL_PREDICT = BuildConfig.BASE_URL_PREDICT

    private val client: OkHttpClient by lazy {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofitAuth: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val retrofitPredict: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_PREDICT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getAuthService(): ApiServiceAuth {
        return retrofitAuth.create(ApiServiceAuth::class.java)
    }

    fun getPredictService(): ApiServicePredict {
        return retrofitPredict.create(ApiServicePredict::class.java)
    }
}

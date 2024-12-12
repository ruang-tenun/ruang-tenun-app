package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private const val BASE_URL = BuildConfig.BASE_URL
    private const val BASE_URL_PREDICT = BuildConfig.BASE_URL_PREDICT

    private val client: OkHttpClient by lazy {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
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
        return retrofit.create(ApiServiceAuth::class.java)
    }

    fun getPredictService(): ApiServicePredict {
        return retrofitPredict.create(ApiServicePredict::class.java)
    }

    fun getCatalogService(): ApiServiceCatalog {
        return retrofit.create(ApiServiceCatalog::class.java)
    }

    fun getProductService(): ApiServiceProduct {
        return retrofit.create(ApiServiceProduct::class.java)
    }
}

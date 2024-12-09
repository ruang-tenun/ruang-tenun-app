package com.ruangtenun.app.data.remote.api

import com.ruangtenun.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL_PREDICT = BuildConfig.BASE_URL_PREDICT
const val BASE_URL_PRODUCT = BuildConfig.BASE_URL_PRODUCT

class ApiConfig {
    companion object {

        fun getPredictService(): ApiServicePredict {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_PREDICT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiServicePredict::class.java)
        }

        fun getProductService(): ApiServiceProduct {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL_PRODUCT)
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()
            return retrofit.create(ApiServiceProduct::class.java)
        }

        fun getCatalogService(): ApiServiceCatalog {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL_PRODUCT)
                .addConverterFactory(GsonConverterFactory.create()).client(client).build()
            return retrofit.create(ApiServiceCatalog::class.java)
        }
    }
}
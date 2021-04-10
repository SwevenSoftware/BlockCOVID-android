package com.sweven.blockcovid.services

import com.sweven.blockcovid.services.OkHttpClient.UnsafeOkHttpClient.unsafeOkHttpClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object NetworkClient {

    private val okHttpClient: OkHttpClient = unsafeOkHttpClient
    private const val url = "https://192.168.210.30:8091"

    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun<T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}


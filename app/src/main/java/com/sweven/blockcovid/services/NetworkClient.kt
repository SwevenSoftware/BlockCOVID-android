package com.sweven.blockcovid.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class NetworkClient {

    private val okHttpClient = UnsafeOkHttpClient().getOkHttpClient()
    private val url = "https://192.168.210.30:8091"

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


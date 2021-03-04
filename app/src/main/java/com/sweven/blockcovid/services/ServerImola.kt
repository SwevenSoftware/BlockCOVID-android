package com.sweven.blockcovid.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object ServerImola {

    // connessione al server Imola, necessaria la connessione con la VPN
    fun connectServer(): Retrofit {
        val url = "http://192.168.210.30:8080"
        val timeout = 10
        val retrofit: Retrofit?
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.connectTimeout(timeout.toLong(), java.util.concurrent.TimeUnit.SECONDS)

        retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClientBuilder.build())
        .build()

        return retrofit
    }
}
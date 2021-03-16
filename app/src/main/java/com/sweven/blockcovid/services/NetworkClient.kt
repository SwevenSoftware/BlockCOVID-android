package com.sweven.blockcovid.services

import com.sweven.blockcovid.services.OkHttpClient.UnsafeOkHttpClient.unsafeOkHttpClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object NetworkClient {
    private const val url = "https://192.168.210.30:8091"
    var retrofit: Retrofit? = null

    val retrofitClient: Retrofit
    get() {
        if (retrofit == null) {
            val okHttpClient: OkHttpClient = unsafeOkHttpClient
            retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
        }
        return retrofit!!
    }
}

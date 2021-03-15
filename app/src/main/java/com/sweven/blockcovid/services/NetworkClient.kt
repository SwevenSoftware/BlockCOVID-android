package com.sweven.blockcovid.services
//
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.scalars.ScalarsConverterFactory
//import java.util.concurrent.TimeUnit
//
//object NetworkClient {
//
//    private const val url = "http://192.168.210.30:8080"
//    const val timeout = 10
//    var retrofit: Retrofit? = null
//
//    val retrofitClient: Retrofit
//    get() {
//        if (retrofit == null) {
//            val okHttpClientBuilder = OkHttpClient.Builder()
//            okHttpClientBuilder.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
//            retrofit = Retrofit.Builder()
//                .baseUrl(url)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .client(ServiceGenerator.generateSecureOkHttpClient(context))
//                .build()
//        }
//        return retrofit!!
//    }
//}

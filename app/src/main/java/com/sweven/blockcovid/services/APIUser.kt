package com.sweven.blockcovid.services

import okhttp3.ResponseBody
import retrofit2.Response
import java.util.HashMap
import retrofit2.http.Multipart
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PartMap

/**
 * Api per ottenere l'elenco utenti
 */
interface APIUser {
    @Multipart
    @Headers("Accept:*/*")
    @POST("/api/login")
    suspend fun loginUser(@PartMap map: HashMap<String?, String?>): Response<ResponseBody>
}

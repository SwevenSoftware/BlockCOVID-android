package com.sweven.blockcovid.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.util.HashMap


/**
 * Api per ottenere l'elenco utenti
 */
interface APIUser {
    @Multipart
    @Headers("Accept:*/*")
    @POST("/api/login")
    suspend fun loginUser(@PartMap map: HashMap<String?, String?>): Response<ResponseBody>
}
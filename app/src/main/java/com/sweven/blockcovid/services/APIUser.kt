package com.sweven.blockcovid.services

import com.sweven.blockcovid.services.gsonReceive.TokenAuthorities
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Api per ottenere l'elenco utenti
 */
interface APIUser {
    @Headers("Accept:*/*")
    @POST("/api/login")
    suspend fun loginUser(@Body requestBody: RequestBody): Response<TokenAuthorities>
}

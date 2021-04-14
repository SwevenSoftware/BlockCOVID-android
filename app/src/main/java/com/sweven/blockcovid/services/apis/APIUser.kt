package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.TokenAuthorities
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Api per ottenere l'elenco utenti
 */
interface APIUser {
    @Headers("Accept:*/*")
    @POST("/api/account/login")
    fun loginUser(@Body requestBody: RequestBody): Call<TokenAuthorities>
}

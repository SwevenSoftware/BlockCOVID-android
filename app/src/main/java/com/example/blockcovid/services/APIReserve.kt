package com.example.blockcovid.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * API for getting User list from https://reqres.in/api/users?&page=1
 */
interface APIReserve {
    @Multipart
    @Headers("Accept:*/*")
    @POST("/rooms/{nameRoom}/reserve")
    suspend fun deskReserve(@Path("nameRoom") nameRoom : String,
                            @Query ("idDesk") idDesk: Int,
                            @Query ("date") date: String,
                            @Query ("from") from: String,
                            @Query ("to") to: String,
                            @Header("Authorization") Autorization: String
                            ): Response<ResponseBody>
}
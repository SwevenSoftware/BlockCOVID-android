package com.example.blockcovid.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalTime

/**
 * API for getting User list from https://reqres.in/api/users?&page=1
 */
interface APIReserve {
    @Multipart
    @Headers("Accept:*/*")
    @POST("/rooms/{nameRoom}/reserve")
    suspend fun deskReserve(@Path("nameRoom") nameRoom : String,
                            @Part ("idDesk") idDesk: Int,
                            @Part ("date") date: String,
                            @Part ("from") from: String,
                            @Part ("to") to: String,
                            @Header("Authorization") authorization: String
                            ): Response<ResponseBody>
}
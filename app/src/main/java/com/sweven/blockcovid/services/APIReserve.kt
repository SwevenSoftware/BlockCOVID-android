package com.sweven.blockcovid.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Part
import retrofit2.http.Multipart
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Header

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIReserve {
    @Multipart
    @Headers("Accept:*/*")
    @POST("/api/rooms/{nameRoom}/reserve")
    suspend fun deskReserve(@Path("nameRoom") nameRoom : String,
                            @Part("idDesk") idDesk: Int,
                            @Part ("date") date: String,
                            @Part ("from") from: String,
                            @Part ("to") to: String,
                            @Header("Authorization") authorization: String
                            ): Response<ResponseBody>
}

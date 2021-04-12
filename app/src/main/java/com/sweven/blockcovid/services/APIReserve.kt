package com.sweven.blockcovid.services

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIReserve {
    @Headers("Accept:*/*")
    @POST("/api/rooms/{nameRoom}/reserve")
    fun deskReserve(@Path("nameRoom") nameRoom : String,
                            @Body requestBody: RequestBody,
                            @Header("Authorization") authorization: String
                            ): Call<ResponseBody>
}

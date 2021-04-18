package com.sweven.blockcovid.services.apis

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIReserve {
    @Headers("Accept:*/*")
    @POST("/api/reservation/new")
    fun deskReserve(@Header("Authorization") authorization: String,
                    @Body requestBody: RequestBody
                    ): Call<ResponseBody>
}
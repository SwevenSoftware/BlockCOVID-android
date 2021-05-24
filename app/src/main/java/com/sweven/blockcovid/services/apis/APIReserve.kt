package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Reservation
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIReserve {
    @Headers("Accept:*/*")
    @POST("/api/reservations/reservation")
    fun deskReserve(
        @Header("Authorization") authorization: String,
        @Body requestBody: RequestBody
    ): Call<Reservation>
}

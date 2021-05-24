package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Reservations
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIReservations {
    @Headers("Accept:*/*")
    @GET("/api/reservations/view/personal")
    fun getReservations(
        @Header("Authorization") authorization: String,
        @Query("from") from: String
    ): Call<Reservations>
}

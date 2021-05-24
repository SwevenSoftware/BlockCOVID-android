package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Reservations
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIDeskReservation {
    @Headers("Accept:*/*")
    @GET("/api/reservations/view/personal/{deskId}")
    fun getDeskReservation(
        @Header("Authorization") authorization: String,
        @Path("deskId") deskId: String,
        @Query("from") from: String
    ): Call<Reservations>
}

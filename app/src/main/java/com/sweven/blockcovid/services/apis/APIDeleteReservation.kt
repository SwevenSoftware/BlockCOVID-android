package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Reservation
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIDeleteReservation {
    @Headers("Accept:*/*")
    @DELETE("/api/reservations/reservation/{idReservation}")
    fun deleteReservation(
        @Header("Authorization") authorization: String,
        @Path("idReservation") idReservation: String
    ): Call<Reservation>
}

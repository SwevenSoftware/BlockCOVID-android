package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Reservation
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIEditReservation {
    @Headers("Accept:*/*")
    @PUT("/api/reservations/reservation/{idReservation}")
    fun editReservation(
        @Header("Authorization") authorization: String,
        @Path("idReservation") idReservation: String,
        @Body requestBody: RequestBody
    ): Call<Reservation>
}

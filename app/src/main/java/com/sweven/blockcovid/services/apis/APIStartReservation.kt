package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Reservation
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * API per iniziare ad usare una postazione
 */
interface APIStartReservation {
    @Headers("Accept:*/*")
    @PUT("/api/reservations/start/{reservationID}")
    fun startReservation(
        @Header("Authorization") authorization: String,
        @Path("reservationID") reservationID: String
    ): Call<Reservation>
}

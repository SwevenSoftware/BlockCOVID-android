package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Reservation
import retrofit2.Call
import retrofit2.http.*

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

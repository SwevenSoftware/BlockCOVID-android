package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Reservation
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * API per iniziare ad usare una postazione
 */
interface APIEndReservation {
    @Headers("Accept:*/*")
    @PUT("/api/reservations/end/{reservationID}")
    fun endReservation(@Header("Authorization") authorization: String,
                       @Path("reservationID") reservationID: String,
                       @Body requestBody: RequestBody
                       ): Call<Reservation>
}

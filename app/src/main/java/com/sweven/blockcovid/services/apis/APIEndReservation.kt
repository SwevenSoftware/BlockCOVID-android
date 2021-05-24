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
 * API per iniziare ad usare una postazione
 */
interface APIEndReservation {
    @Headers("Accept:*/*")
    @PUT("/api/reservations/end/{reservationID}")
    fun endReservation(
        @Header("Authorization") authorization: String,
        @Path("reservationID") reservationID: String,
        @Body requestBody: RequestBody
    ): Call<Reservation>
}

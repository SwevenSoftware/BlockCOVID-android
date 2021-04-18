package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Reservations
import retrofit2.Call
import retrofit2.http.*

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIReservations {
    @Headers("Accept:*/*")
    @GET("/api/reservation/view")
    fun getReservations(@Header("Authorization") authorization: String,
                        @Query("from") from: String,
                        ): Call<Reservations>
}

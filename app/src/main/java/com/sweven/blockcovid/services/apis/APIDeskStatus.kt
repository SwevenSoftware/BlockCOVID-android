package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.DeskStatusLinks
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * API per ottenere lo status di una postazione
 */
interface APIDeskStatus {
    @Headers("Accept:*/*")
    @GET("/api/reservations/desk/{deskId}/{timestamp}")
    fun getDeskStatus(
        @Header("Authorization") authorization: String,
        @Path("timestamp") timestamp: String,
        @Path("deskId") deskId: String,
    ): Call<DeskStatusLinks>
}

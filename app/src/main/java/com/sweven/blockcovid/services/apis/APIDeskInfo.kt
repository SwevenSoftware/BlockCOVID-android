package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.DeskInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * API per ottenere i dati di una postazione
 */

interface APIDeskInfo {
    @Headers("Accept:*/*")
    @GET("/api/rooms/desks/{deskId}")
    fun getDeskInfo(
        @Header("Authorization") authorization: String,
        @Path("deskId") deskId: String,
    ): Call<DeskInfo>
}

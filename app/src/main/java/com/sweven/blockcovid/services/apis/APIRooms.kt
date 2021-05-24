package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.Rooms
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Api per ottenere l'elenco utenti
 */
interface APIRooms {
    @Headers("Accept:*/*")
    @GET("/api/rooms")
    fun getRooms(
        @Header("Authorization") authorization: String,
        @Query("from") from: String,
        @Query("to") to: String,
    ): Call<Rooms>
}

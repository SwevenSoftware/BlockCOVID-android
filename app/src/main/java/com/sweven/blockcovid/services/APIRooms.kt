package com.sweven.blockcovid.services

import com.sweven.blockcovid.services.gsonReceive.Rooms
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * Api per ottenere l'elenco utenti
 */
interface APIRooms {
    @Headers("Accept:*/*")
    @GET("/api/rooms")
    fun getRooms(@Header("Authorization") authorization: String): Call<Rooms>
}

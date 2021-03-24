package com.sweven.blockcovid.services

import com.sweven.blockcovid.services.gsonReceive.Rooms
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Api per ottenere l'elenco utenti
 */
interface APIRooms {
    @Headers("Accept:*/*")
    @GET("/api/rooms")
    suspend fun getRooms(@Header("Authorization") authorization: String): Response<Rooms>
}

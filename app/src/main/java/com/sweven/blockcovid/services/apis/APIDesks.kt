package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.RoomWithDesks
import retrofit2.Call
import retrofit2.http.*

/**
 * API per ottenere i dati di una stanza
 */
interface APIDesks {
    @Headers("Accept:*/*")
    @GET("/api/rooms/{nameRoom}")
    fun getDesks (@Header("Authorization") authorization: String,
                  @Path("nameRoom") nameRoom : String,
                  @Query("from") from: String,
                  @Query("to") to: String
    ): Call<RoomWithDesks>
}

package com.sweven.blockcovid.services

import com.sweven.blockcovid.services.gsonReceive.RoomWithDesks
import retrofit2.Response
import retrofit2.http.*

/**
 * API per segnare una stanza come pulita
 */
interface APIDesks {
    @Headers("Accept:*/*")
    @GET("/api/rooms/{nameRoom}")
    suspend fun getDesks (@Header("Authorization") authorization: String,
                          @Path("nameRoom") nameRoom : String
    ): Response<RoomWithDesks>
}

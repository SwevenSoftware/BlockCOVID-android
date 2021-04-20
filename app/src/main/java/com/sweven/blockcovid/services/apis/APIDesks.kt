package com.sweven.blockcovid.services.apis

import com.sweven.blockcovid.services.gsonReceive.RoomWithDesks
import retrofit2.Call
import retrofit2.http.*

/**
 * API per segnare una stanza come pulita
 */
interface APIDesks {
    @Headers("Accept:*/*")
    @GET("/api/rooms/{nameRoom}")
    fun getDesks (@Header("Authorization") authorization: String,
                  @Path("nameRoom") nameRoom : String
    ): Call<RoomWithDesks>
}

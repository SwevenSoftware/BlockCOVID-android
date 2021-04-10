package com.sweven.blockcovid.services

import com.sweven.blockcovid.services.gsonReceive.Rooms
import retrofit2.Response
import retrofit2.http.*

/**
 * API per segnare una stanza come pulita
 */
interface APIClean {
    @Headers("Accept:*/*")
    @PUT("/api/rooms/{nameRoom}/clean")
    suspend fun cleanRoom (@Header("Authorization") authorization: String,
                           @Path("nameRoom") nameRoom : String
    ): Response<Rooms>
}

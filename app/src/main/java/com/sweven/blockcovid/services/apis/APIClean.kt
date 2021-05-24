package com.sweven.blockcovid.services.apis

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * API per segnare una stanza come pulita
 */
interface APIClean {
    @Headers("Accept:*/*")
    @PUT("/api/rooms/{nameRoom}/clean")
    fun cleanRoom(
        @Header("Authorization") authorization: String,
        @Path("nameRoom") nameRoom: String
    ): Call<ResponseBody>
}

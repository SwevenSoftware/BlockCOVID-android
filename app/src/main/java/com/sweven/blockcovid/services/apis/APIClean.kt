package com.sweven.blockcovid.services.apis

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

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

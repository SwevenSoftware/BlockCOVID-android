package com.sweven.blockcovid.services

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIChangePassword {
    @Headers("Accept:*/*")
    @PUT("/api/user/modify/password")
    suspend fun changePassword (@Header("Authorization") authorization: String,
                                @Body requestBody: RequestBody
                                ): Response<ResponseBody>
}

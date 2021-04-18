package com.sweven.blockcovid.services.apis

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * API per ottenere l'elenco prenotazioni
 */
interface APIChangePassword {
    @Headers("Accept:*/*")
    @PUT("/api/account/modify/password")
    fun changePassword (@Header("Authorization") authorization: String,
                        @Body requestBody: RequestBody
                        ): Call<ResponseBody>
}

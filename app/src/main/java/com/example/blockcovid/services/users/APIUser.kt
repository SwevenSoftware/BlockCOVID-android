package com.example.blockcovid.services.users

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.util.HashMap


/**
 * API for getting User list from https://reqres.in/api/users?&page=1
 */
interface APIUser {
    @Multipart
    @Headers("Accept:*/*")
    @POST("/login")
    //suspend fun uploadLogin(@Part("username") username: RequestBody?, @Part("password") password: RequestBody?): Call<User?>?
    //fun uploadLogin(@PartMap map: HashMap<String?, String?>): Call<User?>
    suspend fun uploadLogin(@PartMap map: HashMap<String?, String?>): Response<ResponseBody>
}
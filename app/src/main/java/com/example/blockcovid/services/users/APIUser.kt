package com.example.blockcovid.services.users

import com.example.blockcovid.services.users.model.BaseUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

/**
 * API for getting User list from https://reqres.in/api/users?&page=1
 */
interface APIUser {
    @Multipart
    @Headers("Accept:*/*")
    @POST("/login")
    suspend fun uploadEmployeeData(@FieldMap params: HashMap<String,Response>): Response<ResponseBody>

    @Multipart
    @Headers("Accept:*/*")
    @PUT("/users/myprofile/photo")
    fun editProfilePhoto(@Header("Authorization") authorization: String, @Part file: MultipartBody.Part): Observable<EditProfilePhotoResponse>
}
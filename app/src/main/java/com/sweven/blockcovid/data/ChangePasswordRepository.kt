package com.sweven.blockcovid.data

import android.widget.Button
import android.widget.Toast
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sweven.blockcovid.R
import com.sweven.blockcovid.services.APIChangePassword
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class ChangePasswordRepository {
    private var netClient = NetworkClient()

    fun setNetwork(nc: NetworkClient) {
        netClient = nc
    }

    suspend fun changePasswordRepo(oldPasswordText:String,newPasswordText:String,authorization:String) {

        val retrofit = netClient.getClient()

        val jsonObject = JSONObject()
        jsonObject.put("old_password", oldPasswordText)
        jsonObject.put("new_password", newPasswordText)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val service = retrofit.create(APIChangePassword::class.java)
            try {
                val response =
                    service.changePassword(authorization, requestBody)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        if (response.errorBody() == null) {
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val responseJson =
                                gson.toJson(JsonParser.parseString(response.body()?.string()))
                            print("Response: ")
                            println(responseJson)
                        } else {
                            val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                            Result.Error(error.error)
                        }
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    Result.Error(error.error)
                }
            } catch (e: Exception) {
                e.message?.let { Result.Error(it) }
            }
        }
    }
}
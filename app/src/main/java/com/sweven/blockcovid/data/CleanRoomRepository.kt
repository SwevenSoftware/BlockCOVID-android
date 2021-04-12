package com.sweven.blockcovid.data

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.R
import com.sweven.blockcovid.services.APIClean
import com.sweven.blockcovid.services.APIRooms
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.gsonReceive.Rooms
import com.sweven.blockcovid.ui.cleanerRooms.RoomsList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanRoomRepository {
    private val _serverResponse = MutableLiveData<Event<Result<String>>>()
    val serverResponse: LiveData<Event<Result<String>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<String>) {
        _serverResponse.value = Event(value)
    }

    fun cleanRoom(authorization: String,nameRoom:String) {

        val call = NetworkClient.buildService(APIClean::class.java).cleanRoom(authorization,nameRoom)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.errorBody() == null) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val responseJson =
                        gson.toJson(JsonParser.parseString(response.body()?.string()))
                    print("Response: ")
                    println(responseJson)
                    val result = Result.Success(responseJson)
                    triggerEvent(result)
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
    }
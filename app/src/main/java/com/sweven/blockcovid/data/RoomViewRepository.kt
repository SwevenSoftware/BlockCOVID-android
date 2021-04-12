package com.sweven.blockcovid.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.services.APIDesks
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.gsonReceive.RoomWithDesks
import com.sweven.blockcovid.ui.roomView.RoomDesks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomViewRepository {

    private val _serverResponse = MutableLiveData<Event<Result<RoomDesks>>>()
    val serverResponse: LiveData<Event<Result<RoomDesks>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<RoomDesks>) {
        _serverResponse.value = Event(value)
    }

    fun showRoom(authorization: String, roomName: String) {

        val call = NetworkClient.buildService(APIDesks::class.java).getDesks(authorization, roomName)

        call.enqueue(object: Callback<RoomWithDesks> {
            override fun onFailure(call: Call<RoomWithDesks>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<RoomWithDesks>, response: Response<RoomWithDesks>) {
                if (response.errorBody() == null) {
                    val desksList = response.body()?.desks
                    println(desksList.toString())
                    desksList?.let {
                        val listSize = desksList.size
                        val idArray = Array(listSize) { _ -> 0}
                        val xArray = Array(listSize) { _ -> 0}
                        val yArray = Array(listSize) { _ -> 0}

                        for (i in idArray.indices) {
                            idArray[i] = i + 1
                            xArray[i] = desksList[i].x - 1
                            yArray[i] = desksList[i].y - 1
                        }
                        val desks = Result.Success(RoomDesks(idArray, xArray, yArray))
                        triggerEvent(desks)
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
}
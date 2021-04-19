package com.sweven.blockcovid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.apis.APIDesks
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.gsonReceive.RoomWithDesks
import com.sweven.blockcovid.data.model.RoomDesks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomViewRepository(private val networkClient: NetworkClient) {

    private val _serverResponse = MutableLiveData<Event<Result<RoomDesks>>>()
    val serverResponse: LiveData<Event<Result<RoomDesks>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<RoomDesks>) {
        _serverResponse.value = Event(value)
    }

    fun showRoom(authorization: String, roomName: String) {

        val call = networkClient.buildService(APIDesks::class.java).getDesks(authorization, roomName)

        call.enqueue(object: Callback<RoomWithDesks> {
            override fun onFailure(call: Call<RoomWithDesks>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<RoomWithDesks>, response: Response<RoomWithDesks>) {
                if (response.errorBody() == null) {
                    val desksList = response.body()?.desks
                    println(desksList.toString())
                    if (desksList != null) {
                        val listSize = desksList.size
                        val idArray = Array(listSize) {""}
                        val xArray = Array(listSize) {0}
                        val yArray = Array(listSize) {0}

                        for (i in idArray.indices) {
                            idArray[i] = (desksList[i].deskId)
                            xArray[i] = (desksList[i].x - 1).toInt()
                            yArray[i] = (desksList[i].y - 1).toInt()
                        }
                        val desks = Result.Success(RoomDesks(idArray, xArray, yArray))
                        triggerEvent(desks)
                    } else {
                        triggerEvent(Result.Success(RoomDesks(null, null, null)))
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
}
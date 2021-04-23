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

    fun showRoom(arrivalDateTime: String, exitDateTime: String, authorization: String, roomName: String) {

        val call = networkClient.buildService(APIDesks::class.java).getDesks(authorization, roomName, arrivalDateTime, exitDateTime)

        call.enqueue(object: Callback<RoomWithDesks> {
            override fun onFailure(call: Call<RoomWithDesks>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<RoomWithDesks>, response: Response<RoomWithDesks>) {
                if (response.errorBody() == null) {
                    val room = response.body()
                    if (room != null) {
                        val listSize = room.desks.size
                        val openingTime = room.room.openingTime
                        val closingTime = room.room.closingTime
                        val openingDays = Array(listSize) {""}
                        val idArray = Array(listSize) {""}
                        val xArray = Array(listSize) {0}
                        val yArray = Array(listSize) {0}
                        val availableArray = Array(listSize) {false}

                        for (i in room.room.openingDays.indices) {
                            openingDays[i] = room.room.openingDays[i]
                        }

                        for (l in idArray.indices) {
                            idArray[l] = (room.desks[l].deskId)
                            xArray[l] = (room.desks[l].x - 1).toInt()
                            yArray[l] = (room.desks[l].y - 1).toInt()
                            availableArray[l] = room.desks[l].available
                        }
                        val desks = Result.Success(RoomDesks(
                            openingTime, closingTime, openingDays,
                            idArray, xArray, yArray, availableArray
                        ))
                        triggerEvent(desks)
                    } else {
                        triggerEvent(Result.Success(RoomDesks(
                            null, null, null,
                            null, null, null, null
                        )))
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
}
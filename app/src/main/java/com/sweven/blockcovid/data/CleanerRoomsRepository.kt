package com.sweven.blockcovid.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.services.APIRooms
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.gsonReceive.Rooms
import com.sweven.blockcovid.ui.cleanerRooms.RoomsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CleanerRoomsRepository {

    private val _serverResponse = MutableLiveData<Event<Result<RoomsList>>>()
    val serverResponse: LiveData<Event<Result<RoomsList>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<RoomsList>) {
        _serverResponse.value = Event(value)
    }

    fun cleanerRooms(authorization: String) {

        val call = NetworkClient.buildService(APIRooms::class.java).getRooms(authorization)

        call.enqueue(object : Callback<Rooms> {
            override fun onFailure(call: Call<Rooms>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<Rooms>, response: Response<Rooms>) {
                if (response.errorBody() == null) {
                    val roomList = response.body()?.embedded?.roomWithDesksList
                    roomList?.let {
                        val listSize = roomList.size
                        val nameArray = Array(listSize) { _ -> ""}
                        val isCleanArray = Array(listSize) { _ -> false}
                        for (i in 0 until listSize) {
                            nameArray[i] = roomList[i].room.name
                            isCleanArray[i] = roomList[i].room.roomStatus == "CLEAN"
                        }
                        val roomsList = Result.Success(RoomsList(nameArray,isCleanArray))
                        triggerEvent(roomsList)
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
}
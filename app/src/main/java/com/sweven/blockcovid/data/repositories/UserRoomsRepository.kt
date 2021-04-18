package com.sweven.blockcovid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.apis.APIRooms
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.gsonReceive.Rooms
import com.sweven.blockcovid.data.model.UserRoomsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


class UserRoomsRepository(private val networkClient: NetworkClient) {

    private val _serverResponse = MutableLiveData<Event<Result<UserRoomsList>>>()
    val serverResponse: LiveData<Event<Result<UserRoomsList>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<UserRoomsList>) {
        _serverResponse.value = Event(value)
    }

    fun userRooms(authorization: String) {

        val call = networkClient.buildService(APIRooms::class.java).getRooms(authorization)

        call.enqueue(object : Callback<Rooms> {
            override fun onFailure(call: Call<Rooms>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<Rooms>, response: Response<Rooms>) {
                if (response.errorBody() == null) {
                    val roomList = response.body()?.embedded?.roomWithDesksList
                    if (roomList != null) {
                        val listSize = roomList.size
                        val nameArray = Array(listSize) {""}
                        val openArray = Array(listSize) {""}
                        val closeArray = Array(listSize) {""}
                        val daysArray = Array(listSize) {Array(7){""}}
                        val isOpenArray = Array(listSize) {false}
                        for (i in 0 until listSize) {
                            nameArray[i] = roomList[i].room.name
                            openArray[i] = roomList[i].room.openingTime.dropLast(3)
                            closeArray[i] = roomList[i].room.closingTime.dropLast(3)

                            for (l in roomList[i].room.openingDays.indices) {
                                daysArray[i][l] = roomList[i].room.openingDays[l]
                            }

                            if (isOpen(openArray[i], closeArray[i], daysArray[i]) && !roomList[i].room.closed) {
                                isOpenArray[i] = true
                            }
                        }
                        val roomsList = Result.Success(UserRoomsList(nameArray, openArray, closeArray, daysArray, isOpenArray))
                        triggerEvent(roomsList)
                    } else {
                        triggerEvent(Result.Success(UserRoomsList(null, null, null, null, null)))
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }

    fun isOpen (ot: String, ct: String, day: Array<String>): Boolean {
        val openingTime = LocalTime.parse(ot)
        val closingTime = LocalTime.parse(ct)
        val nowTime = LocalTime.now()

        var todayOpen = false
        val thisDay = LocalDate.now().dayOfWeek.toString().toUpperCase(Locale.ITALIAN)
        for (i in day.indices) {
            if (thisDay == day[i]) {
                todayOpen = true
            }
        }
        return openingTime < nowTime && closingTime > nowTime && todayOpen
    }
}
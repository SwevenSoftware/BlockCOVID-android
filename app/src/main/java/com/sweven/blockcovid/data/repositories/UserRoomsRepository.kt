package com.sweven.blockcovid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.UserRoomsList
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIRooms
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.gsonReceive.Rooms
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.TimeZone

class UserRoomsRepository(private val networkClient: NetworkClient) {

    private val _serverResponse = MutableLiveData<Event<Result<UserRoomsList>>>()
    val serverResponse: LiveData<Event<Result<UserRoomsList>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<UserRoomsList>) {
        _serverResponse.value = Event(value)
    }

    fun userRooms(authorization: String) {

        val nowLocalDateTime = LocalDateTime.now(UTC).truncatedTo(ChronoUnit.MINUTES).toString()
        val call = networkClient.buildService(APIRooms::class.java).getRooms(authorization, nowLocalDateTime, nowLocalDateTime)

        call.enqueue(object : Callback<Rooms> {
            override fun onFailure(call: Call<Rooms>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<Rooms>, response: Response<Rooms>) {
                if (response.errorBody() == null) {
                    val roomList = response.body()?.embedded?.roomWithDesksList
                    if (roomList != null) {
                        val listSize = roomList.size
                        val nameArray = Array(listSize) { "" }
                        val openArray = Array(listSize) { "" }
                        val closeArray = Array(listSize) { "" }
                        val daysArray = Array(listSize) { Array(7) { "" } }
                        val isOpenArray = Array(listSize) { false }
                        for (i in 0 until listSize) {
                            nameArray[i] = roomList[i].room.name

                            val openingTime = utcToLocalTime(roomList[i].room.openingTime.dropLast(3))
                            val closingTime = utcToLocalTime(roomList[i].room.closingTime.dropLast(3))

                            openArray[i] = openingTime
                            closeArray[i] = closingTime

                            for (l in roomList[i].room.openingDays.indices) {
                                daysArray[i][l] = roomList[i].room.openingDays[l]
                            }

                            if (isOpen(openArray[i], closeArray[i], daysArray[i]) && !roomList[i].room.closed) {
                                isOpenArray[i] = true
                            }
                        }
                        triggerEvent(Result.Success(UserRoomsList(nameArray, openArray, closeArray, daysArray, isOpenArray)))
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

    fun isOpen(ot: String, ct: String, day: Array<String>): Boolean {
        val openingTime = LocalTime.parse(ot)
        val closingTime = LocalTime.parse(ct)
        val nowTime = LocalTime.now(TimeZone.getDefault().toZoneId())

        var todayOpen = false
        val thisDay = LocalDate.now(TimeZone.getDefault().toZoneId()).dayOfWeek.toString().uppercase()
        for (i in day.indices) {
            if (thisDay == day[i]) {
                todayOpen = true
            }
        }
        return openingTime < nowTime && closingTime > nowTime && todayOpen
    }

    fun utcToLocalTime(time: String): String {
        val localTime = LocalTime.parse(time)
        val localDate = LocalDate.now(UTC)
        val zonedTimeDate = ZonedDateTime.of(localDate, localTime, UTC)
        return zonedTimeDate.withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalTime().toString()
    }
}

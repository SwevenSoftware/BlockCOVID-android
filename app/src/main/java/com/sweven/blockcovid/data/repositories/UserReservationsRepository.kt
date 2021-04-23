package com.sweven.blockcovid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.UserReservationsList
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIReservations
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.gsonReceive.Reservations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.*
import java.util.*

class UserReservationsRepository(private val networkClient: NetworkClient) {

    private val _serverResponse = MutableLiveData<Event<Result<UserReservationsList>>>()
    val serverResponse: LiveData<Event<Result<UserReservationsList>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<UserReservationsList>) {
        _serverResponse.value = Event(value)
    }

    fun userReservations(authorization: String, from: String) {

        val call = networkClient.buildService(APIReservations::class.java).getReservations(authorization, from)

        call.enqueue(object : Callback<Reservations> {
            override fun onFailure(call: Call<Reservations>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<Reservations>, response: Response<Reservations>) {
                if (response.errorBody() == null) {
                    val reservationList = response.body()?.embedded?.reservationWithRoomList
                    if (reservationList != null) {
                        val listSize = reservationList.size
                        val reservationId = Array(listSize) {""}
                        val deskIdArray = Array(listSize) {""}
                        val roomArray = Array(listSize) {""}
                        val startArray = Array(listSize) {""}
                        val endArray = Array(listSize) {""}
                        val dayArray = Array(listSize) {""}
                        for (i in 0 until listSize) {
                            val startDateTimeUTC = reservationList[i].start
                            val endDateTimeUTC = reservationList[i].end

                            val startTime = UTCToLocalDateTime(startDateTimeUTC).toLocalTime().toString()
                            val endTime = UTCToLocalDateTime(endDateTimeUTC).toLocalTime().toString()
                            val day = UTCToLocalDateTime(startDateTimeUTC).toLocalDate().toString()

                            reservationId[i] = reservationList[i].id
                            deskIdArray[i] = reservationList[i].deskID
                            roomArray[i] = reservationList[i].room
                            startArray[i] = startTime
                            endArray[i] = endTime
                            dayArray[i] = day
                        }
                        val reservationsList = Result.Success(UserReservationsList(reservationId, deskIdArray, roomArray, startArray, endArray, dayArray))
                        triggerEvent(reservationsList)
                    } else {
                        triggerEvent(Result.Success(UserReservationsList(null, null, null, null, null, null)))
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }

    fun UTCToLocalDateTime(dateTime: String): ZonedDateTime {
        val localDateTime = LocalDateTime.parse(dateTime)
        val zonedTimeDate = ZonedDateTime.of(localDateTime, ZoneOffset.UTC)
        return zonedTimeDate.withZoneSameInstant(TimeZone.getDefault().toZoneId())
    }
}
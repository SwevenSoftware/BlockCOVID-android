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
import java.time.LocalDateTime

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
                println(response.body().toString())
                if (response.errorBody() == null) {
                    val reservationList = response.body()?.content
                    if (reservationList != null) {
                        val listSize = reservationList.size
                        val reservationId = Array(listSize) {""}
                        val nameArray = Array(listSize) {""}
                        val startArray = Array(listSize) {""}
                        val endArray = Array(listSize) {""}
                        val dayArray = Array(listSize) {""}
                        val roomArray = Array(listSize) {""}
                        for (i in 0 until listSize) {
                            val startTime = LocalDateTime.parse(reservationList[i].start).toLocalTime().toString()
                            val endTime = LocalDateTime.parse(reservationList[i].end).toLocalTime().toString()
                            val day = LocalDateTime.parse(reservationList[i].start).toLocalDate().toString()
                            reservationId[i] = reservationList[i].id
                            nameArray[i] = reservationList[i].deskID
                            startArray[i] = startTime
                            endArray[i] = endTime
                            dayArray[i] = day
                            roomArray[i] = reservationList[i].room
                        }
                        val reservationsList = Result.Success(UserReservationsList(reservationId, nameArray, startArray, endArray, dayArray, roomArray))
                        triggerEvent(reservationsList)
                    } else {
                        triggerEvent(Result.Success(UserReservationsList(null, null, null, null, null, null)))
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    println(error.error)
                    println(response.code())
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
}
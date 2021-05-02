package com.sweven.blockcovid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.DeskReservationData
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIDeskReservation
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.gsonReceive.Reservations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*

class DeskReservationRepository(private val networkClient: NetworkClient) {

    private val _serverResponse = MutableLiveData<Event<Result<DeskReservationData>>>()
    val serverResponse: LiveData<Event<Result<DeskReservationData>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<DeskReservationData>) {
        _serverResponse.value = Event(value)
    }

    fun deskReservation(authorization: String, deskId: String, from: String,) {

        val call = networkClient.buildService(APIDeskReservation::class.java).getDeskReservation(authorization, deskId, from)

        call.enqueue(object : Callback<Reservations> {
            override fun onFailure(call: Call<Reservations>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<Reservations>, response: Response<Reservations>) {
                if (response.errorBody() == null) {
                    val reservation = response.body()?.embedded?.reservationWithRoomList
                    if (reservation != null) {
                        var earliestReservation = 0
                        var previousStart = LocalDateTime.parse(reservation[0].start)
                        // finds the earlies reservation
                        for (i in 1 until reservation.size) {
                            val thisStart = LocalDateTime.parse(reservation[i].start)
                            if (thisStart < previousStart) {
                                earliestReservation = i
                            }
                            previousStart = thisStart
                        }
                        val id = reservation[earliestReservation].id
                        val room = reservation[earliestReservation].room
                        val start = UTCToLocalDateTime(reservation[earliestReservation].start).toString()
                        val end = UTCToLocalDateTime(reservation[earliestReservation].end).toString()
                        var usageStart = reservation[earliestReservation].usageStart
                        if (usageStart != null) {
                            usageStart = UTCToLocalDateTime(usageStart).toString()
                        }
                        var usageEnd = reservation[earliestReservation].usageEnd
                        if (usageEnd != null) {
                            usageEnd = UTCToLocalDateTime(usageEnd).toString()
                        }
                        val clean = reservation[earliestReservation].deskCleaned

                        triggerEvent(Result.Success(DeskReservationData(id, room, start, end, usageStart, usageEnd, clean)))
                    } else {
                        triggerEvent(Result.Success(DeskReservationData(
                            null, null, null, null, null, null, null
                        )))
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }

    fun UTCToLocalDateTime(dateTime: String): LocalDateTime {
        val localDateTime = LocalDateTime.parse(dateTime)
        val zonedTimeDate = ZonedDateTime.of(localDateTime, ZoneOffset.UTC)
        return zonedTimeDate.withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime()
    }
}
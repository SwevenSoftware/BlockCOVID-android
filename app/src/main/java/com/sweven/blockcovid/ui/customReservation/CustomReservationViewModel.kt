package com.sweven.blockcovid.ui.customReservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.R
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.RoomDesks
import com.sweven.blockcovid.data.repositories.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class CustomReservationViewModel(
    private val reservationRepository: ReservationRepository,
    private val roomViewRepository: RoomViewRepository
) : ViewModel() {

    private val _reservationResult = MutableLiveData<CustomReservationResult>()
    val reservationResult: LiveData<CustomReservationResult>
        get() = _reservationResult

    fun customReservation(deskId: String, start: String, end: String, authorization: String) {
        reservationRepository.reserve(deskId, start, end, authorization)
        reservationRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _reservationResult.postValue(CustomReservationResult(success = it.data))
                } else if (it is Result.Error) {
                    _reservationResult.postValue(CustomReservationResult(error = it.exception))
                }
            }
        }
    }

    private val _roomViewResult = MutableLiveData<RoomViewResult>()
    val roomViewResult: LiveData<RoomViewResult>
        get() = _roomViewResult

    fun showRoom(arrivalDateTime: String, exitDateTime: String, authorization: String, roomName: String) {
        roomViewRepository.showRoom(arrivalDateTime, exitDateTime, authorization, roomName)
        roomViewRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _roomViewResult.postValue(
                        RoomViewResult(
                            success =
                            RoomDesks(
                                openingTime = it.data.openingTime, closingTime = it.data.closingTime,
                                openingDays = it.data.openingDays, idArray = it.data.idArray, xArray = it.data.xArray,
                                yArray = it.data.yArray, availableArray = it.data.availableArray
                            )
                        )
                    )
                } else if (it is Result.Error) {
                    _roomViewResult.postValue(RoomViewResult(error = it.exception))
                }
            }
        }
    }

    private val _customReservationForm = MutableLiveData<CustomReservationFormState>()
    val customReservationForm: LiveData<CustomReservationFormState> = _customReservationForm

    fun inputDataChanged(
        localDateTime: LocalDateTime,
        arrivalTime: String,
        exitTime: String,
        selectedDate: String,
        openingTime: String,
        closingTime: String,
        daysOpen: Array<String>
    ) {
        val localToday = localDateTime.toLocalDate()
        val localArrivalDateTime = LocalDateTime.of(LocalDate.parse(selectedDate), LocalTime.parse(arrivalTime))

        val localSelected = LocalDate.parse(selectedDate)

        if (arrivalTime > exitTime) {
            _customReservationForm.value = CustomReservationFormState(
                arrivalTimeError = R.string.invalid_time,
                exitTimeError = R.string.invalid_time
            )
        } else if (localSelected < localToday) {
            _customReservationForm.value = CustomReservationFormState(
                selectedDateError = R.string.invalid_date
            )
        } else if (localArrivalDateTime < localDateTime) {
            _customReservationForm.value = CustomReservationFormState(
                arrivalTimeError = R.string.reservation_old,
                exitTimeError = R.string.reservation_old,
                selectedDateError = R.string.reservation_old
            )
        } else if (!isOpenInterval(arrivalTime, exitTime, selectedDate, openingTime, closingTime, daysOpen)) {
            _customReservationForm.value = CustomReservationFormState(
                arrivalTimeError = R.string.room_is_closed,
                exitTimeError = R.string.room_is_closed,
                selectedDateError = R.string.room_is_closed_day
            )
        } else {
            _customReservationForm.value = CustomReservationFormState(isDataValid = true)
        }
    }

    fun isOpenInterval(at: String, et: String, sd: String, ot: String, ct: String, openDays: Array<String>): Boolean {
        val arrivalTime = LocalTime.parse(at)
        val exitTime = LocalTime.parse(et)
        val selectedDate = LocalDate.parse(sd).dayOfWeek.toString().toUpperCase(Locale.ITALIAN)
        val openingTime = LocalTime.parse(ot)
        val closingTime = LocalTime.parse(ct)

        var todayOpen = false
        for (i in openDays.indices) {
            if (selectedDate == openDays[i]) {
                todayOpen = true
            }
        }
        return openingTime < arrivalTime && closingTime > exitTime && todayOpen
    }
}

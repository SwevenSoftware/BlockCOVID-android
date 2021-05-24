package com.sweven.blockcovid.ui.editReservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.R
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.RoomDesks
import com.sweven.blockcovid.data.repositories.DeleteReservationRepository
import com.sweven.blockcovid.data.repositories.EditReservationRepository
import com.sweven.blockcovid.data.repositories.RoomViewRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale

class EditReservationViewModel(
    private val editReservationRepository: EditReservationRepository,
    private val deleteReservationRepository: DeleteReservationRepository,
    private val roomViewRepository: RoomViewRepository
) : ViewModel() {

    private val _editReservationResult = MutableLiveData<EditReservationResult>()
    val editReservationResult: LiveData<EditReservationResult>
        get() = _editReservationResult

    fun editReservation(idReservation: String, deskId: String, start: String, end: String, authorization: String) {
        editReservationRepository.editReservation(idReservation, deskId, start, end, authorization)
        editReservationRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _editReservationResult.postValue(EditReservationResult(success = it.data))
                } else if (it is Result.Error) {
                    _editReservationResult.postValue(EditReservationResult(error = it.exception))
                }
            }
        }
    }

    private val _deleteReservationResult = MutableLiveData<DeleteReservationResult>()
    val deleteReservationResult: LiveData<DeleteReservationResult>
        get() = _deleteReservationResult

    fun deleteReservation(idReservation: String, authorization: String) {
        deleteReservationRepository.deleteReservation(idReservation, authorization)
        deleteReservationRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _deleteReservationResult.postValue(DeleteReservationResult(success = it.data))
                } else if (it is Result.Error) {
                    _deleteReservationResult.postValue(DeleteReservationResult(error = it.exception))
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

    private val _editReservationForm = MutableLiveData<EditReservationFormState>()
    val editReservationForm: LiveData<EditReservationFormState> = _editReservationForm

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
            _editReservationForm.value = EditReservationFormState(
                arrivalTimeError = R.string.invalid_time,
                exitTimeError = R.string.invalid_time
            )
        } else if (localSelected < localToday) {
            _editReservationForm.value = EditReservationFormState(
                selectedDateError = R.string.invalid_date
            )
        } else if (localArrivalDateTime < localDateTime) {
            _editReservationForm.value = EditReservationFormState(
                arrivalTimeError = R.string.reservation_old,
                exitTimeError = R.string.reservation_old,
                selectedDateError = R.string.reservation_old
            )
        } else if (!isOpenInterval(arrivalTime, exitTime, selectedDate, openingTime, closingTime, daysOpen)) {
            _editReservationForm.value = EditReservationFormState(
                arrivalTimeError = R.string.room_is_closed,
                exitTimeError = R.string.room_is_closed,
                selectedDateError = R.string.room_is_closed_day
            )
        } else {
            _editReservationForm.value = EditReservationFormState(isDataValid = true)
        }
    }

    fun isOpenInterval(at: String, et: String, sd: String, ot: String, ct: String, openDays: Array<String>): Boolean {
        val arrivalTime = LocalTime.parse(at)
        val exitTime = LocalTime.parse(et)
        val selectedDate = LocalDate.parse(sd).dayOfWeek.toString().uppercase(Locale.ITALIAN)
        val openingTime = LocalTime.parse(ot)
        val closingTime = LocalTime.parse(ct)

        var todayOpen = false
        for (i in openDays.indices) {
            if (selectedDate == openDays[i]) {
                todayOpen = true
            }
        }
        return openingTime <= arrivalTime && closingTime >= exitTime && todayOpen
    }
}

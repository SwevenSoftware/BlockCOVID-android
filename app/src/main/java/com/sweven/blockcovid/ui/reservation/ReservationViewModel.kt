package com.sweven.blockcovid.ui.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.InputChecks
import com.sweven.blockcovid.R
import com.sweven.blockcovid.data.repositories.ReservationRepository
import com.sweven.blockcovid.data.Result

class ReservationViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {

    private val _reservationForm = MutableLiveData<ReservationFormState>()
    val reservationFormState: LiveData<ReservationFormState> = _reservationForm

    private val _reservationResult = MutableLiveData<ReservationResult>()
    val reservationResult: LiveData<ReservationResult>
        get() = _reservationResult

    fun reserve(deskId: String, start: String, end: String, authorization: String) {
        reservationRepository.reserve(deskId, start, end, authorization)
        reservationRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _reservationResult.postValue(ReservationResult(success = it.data))
                } else if (it is Result.Error) {
                    _reservationResult.postValue(ReservationResult(error = it.exception))
                }
            }
        }
    }

    fun inputDataChanged(arrivalTime: String, exitTime: String, selectedDate: String) {
        if (!InputChecks.isFieldNotEmpty(arrivalTime)) {
            _reservationForm.value = ReservationFormState(arrivalTimeError = R.string.empty_field)
        } else if (!InputChecks.isFieldNotEmpty(exitTime)) {
            _reservationForm.value = ReservationFormState(exitTimeError = R.string.empty_field)
        } else if (!InputChecks.isFieldNotEmpty(selectedDate)) {
            _reservationForm.value = ReservationFormState(selectedDateError = R.string.empty_field)
        }
        else {
            _reservationForm.value = ReservationFormState(isDataValid = true)
        }
    }
}

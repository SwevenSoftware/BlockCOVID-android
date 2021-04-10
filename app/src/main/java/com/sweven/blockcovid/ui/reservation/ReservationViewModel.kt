package com.sweven.blockcovid.ui.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.InputChecks
import com.sweven.blockcovid.R

class ReservationViewModel : ViewModel() {

    private val _reservationForm = MutableLiveData<ReservationFormState>()
    val reservationFormState: LiveData<ReservationFormState> = _reservationForm

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

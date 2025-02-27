package com.sweven.blockcovid.ui.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.repositories.ReservationRepository

class ReservationViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {

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
}

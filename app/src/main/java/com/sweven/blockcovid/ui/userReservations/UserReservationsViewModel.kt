package com.sweven.blockcovid.ui.userReservations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.UserReservationsList
import com.sweven.blockcovid.data.repositories.UserReservationsRepository


class UserReservationsViewModel(private val userReservationsRepository: UserReservationsRepository) : ViewModel() {
    private val _userReservationsResult = MutableLiveData<UserReservationsResult>()
    val userReservationsResult: LiveData<UserReservationsResult>
        get() = _userReservationsResult

    fun showReservations(authorization: String, from: String) {
        userReservationsRepository.userReservations(authorization, from)
        userReservationsRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _userReservationsResult.postValue(
                        UserReservationsResult(success =
                            UserReservationsList(
                                reservationId = it.data.reservationId,
                                deskId = it.data.deskId,
                                room = it.data.room,
                                startTime = it.data.startTime,
                                endTime = it.data.endTime,
                                day = it.data.day,
                            )
                        )
                    )
                } else if (it is Result.Error) {
                    _userReservationsResult.postValue(UserReservationsResult(error = it.exception))
                }
            }
        }
    }
}

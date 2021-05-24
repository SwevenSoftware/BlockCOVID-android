package com.sweven.blockcovid.ui.userRooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.UserRoomsList
import com.sweven.blockcovid.data.repositories.UserRoomsRepository

class UserRoomsViewModel(private val userRoomsRepository: UserRoomsRepository) : ViewModel() {
    private val _userRoomsResult = MutableLiveData<UserRoomsResult>()
    val userRoomsResult: LiveData<UserRoomsResult>
        get() = _userRoomsResult

    fun showRooms(authorization: String) {
        userRoomsRepository.userRooms(authorization)
        userRoomsRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _userRoomsResult.postValue(
                        UserRoomsResult(
                            success =
                            UserRoomsList(
                                roomName = it.data.roomName,
                                roomOpen = it.data.roomOpen,
                                roomClose = it.data.roomClose,
                                roomDays = it.data.roomDays,
                                roomIsOpen = it.data.roomIsOpen
                            )
                        )
                    )
                } else if (it is Result.Error) {
                    _userRoomsResult.postValue(UserRoomsResult(error = it.exception))
                }
            }
        }
    }
}

package com.sweven.blockcovid.ui.cleanerRooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.CleanerRoomsList
import com.sweven.blockcovid.data.repositories.CleanRoomRepository
import com.sweven.blockcovid.data.repositories.CleanerRoomsRepository

class CleanerRoomsViewModel(private val cleanerRoomsRepository: CleanerRoomsRepository, private val cleanRoomRepository: CleanRoomRepository) :
    ViewModel() {

    private val _cleanerRoomsResult = MutableLiveData<CleanerRoomsResult>()
    val cleanerRoomsResult: LiveData<CleanerRoomsResult>
        get() = _cleanerRoomsResult

    fun showRooms(authorization: String) {
        cleanerRoomsRepository.cleanerRooms(authorization)
        cleanerRoomsRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _cleanerRoomsResult.postValue(
                        CleanerRoomsResult(
                            success =
                            CleanerRoomsList(
                                roomName = it.data.roomName, roomIsCleaned = it.data.roomIsCleaned, roomIsOpen = it.data.roomIsOpen
                            )
                        )
                    )
                } else if (it is Result.Error) {
                    _cleanerRoomsResult.postValue(CleanerRoomsResult(error = it.exception))
                }
            }
        }
    }

    private val _cleanRoomResult = MutableLiveData<CleanRoomResult>()
    val cleanRoomResult: LiveData<CleanRoomResult>
        get() = _cleanRoomResult

    fun cleanRoom(authorization: String, roomName: String) {
        cleanRoomRepository.cleanRoom(authorization, roomName)
        cleanRoomRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _cleanRoomResult.postValue(
                        CleanRoomResult(success = it.data)
                    )
                } else if (it is Result.Error) {
                    _cleanRoomResult.postValue(CleanRoomResult(error = it.exception))
                }
            }
        }
    }
}

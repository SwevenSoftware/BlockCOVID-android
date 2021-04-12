package com.sweven.blockcovid.ui.cleanerRooms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.data.CleanerRoomsRepository
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.CleanerRoomsList

class CleanerRoomsViewModel (private val cleanerRoomsRepository: CleanerRoomsRepository) :
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
                        CleanerRoomsResult(success =
                    CleanerRoomsList(
                        roomName = it.data.roomName, roomIsCleaned = it.data.roomIsCleaned)
                        )
                    )
                } else if (it is Result.Error) {
                    _cleanerRoomsResult.postValue(CleanerRoomsResult(error = it.exception))
                }
            }
        }
    }
}

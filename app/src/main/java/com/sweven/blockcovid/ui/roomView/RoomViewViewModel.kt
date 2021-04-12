package com.sweven.blockcovid.ui.roomView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.RoomViewRepository
import com.sweven.blockcovid.data.model.RoomDesks

class RoomViewViewModel(private val roomViewRepository: RoomViewRepository) : ViewModel() {

    private val _roomViewResult = MutableLiveData<RoomViewResult>()
    val roomViewResult: LiveData<RoomViewResult>
        get() = _roomViewResult

    fun showRooms(authorization: String, roomName: String) {
        roomViewRepository.showRoom(authorization, roomName)
        roomViewRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _roomViewResult.postValue(RoomViewResult(success =
                    RoomDesks(
                            idArray = it.data.idArray, xArray = it.data.xArray,  yArray = it.data.yArray)))
                } else if (it is Result.Error) {
                    _roomViewResult.postValue(RoomViewResult(error = it.exception))
                }
            }
        }
    }
}

package com.sweven.blockcovid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.DeskStatus
import com.sweven.blockcovid.data.repositories.DeskStatusRepository

class HomeViewModel (private val deskStatusRepository: DeskStatusRepository) : ViewModel() {

    private val _deskStatusResult = MutableLiveData<DeskStatusResult>()
    val deskStatusResult: LiveData<DeskStatusResult>
        get() = _deskStatusResult

    fun deskStatus(authorization: String, timestamp: String, deskId: String) {
        deskStatusRepository.deskStatus(authorization, timestamp, deskId)
        deskStatusRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _deskStatusResult.postValue(
                        DeskStatusResult(success =
                            DeskStatus(available = it.data.available, nextChange = it.data.nextChange)
                    ))
                } else if (it is Result.Error) {
                    _deskStatusResult.postValue(DeskStatusResult(error = it.exception))
                }
            }
        }
    }
}

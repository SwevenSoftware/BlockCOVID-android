package com.sweven.blockcovid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.DeskReservationData
import com.sweven.blockcovid.data.model.DeskStatus
import com.sweven.blockcovid.data.model.ThisDeskInfo
import com.sweven.blockcovid.data.repositories.*

class HomeViewModel(
    private val deskStatusRepository: DeskStatusRepository,
    private val deskInfoRepository: DeskInfoRepository,
    private val deskReservationRepository: DeskReservationRepository,
    private val startReservationRepository: StartReservationRepository,
    private val endReservationRepository: EndReservationRepository,
    private val deleteReservationRepository: DeleteReservationRepository
) : ViewModel() {

    private val _deskStatusResult = MutableLiveData<DeskStatusResult>()
    val deskStatusResult: LiveData<DeskStatusResult>
        get() = _deskStatusResult

    fun deskStatus(authorization: String, timestamp: String, deskId: String) {
        deskStatusRepository.deskStatus(authorization, timestamp, deskId)
        deskStatusRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _deskStatusResult.postValue(
                        DeskStatusResult(
                            success =
                            DeskStatus(it.data.available, it.data.nextChange)
                        )
                    )
                } else if (it is Result.Error) {
                    _deskStatusResult.postValue(DeskStatusResult(error = it.exception))
                }
            }
        }
    }

    private val _deskInfoResult = MutableLiveData<DeskInfoResult>()
    val deskInfoResult: LiveData<DeskInfoResult>
        get() = _deskInfoResult

    fun deskInfo(authorization: String, deskId: String) {
        deskInfoRepository.deskInfo(authorization, deskId)
        deskInfoRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _deskInfoResult.postValue(
                        DeskInfoResult(
                            success =
                            ThisDeskInfo(
                                x = it.data.x, y = it.data.y,
                                room = it.data.room, deskClean = it.data.deskClean
                            )
                        )
                    )
                } else if (it is Result.Error) {
                    _deskInfoResult.postValue(DeskInfoResult(error = it.exception))
                }
            }
        }
    }

    private val _deskReservationResult = MutableLiveData<DeskReservationResult>()
    val deskReservationResult: LiveData<DeskReservationResult>
        get() = _deskReservationResult

    fun deskReservation(authorization: String, deskId: String, timestamp: String) {
        deskReservationRepository.deskReservation(authorization, deskId, timestamp,)
        deskReservationRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _deskReservationResult.postValue(
                        DeskReservationResult(
                            success =
                            DeskReservationData(
                                id = it.data.id, room = it.data.room, start = it.data.start, end = it.data.end,
                                usageStart = it.data.usageStart, usageEnd = it.data.usageEnd, clean = it.data.clean
                            )
                        )
                    )
                } else if (it is Result.Error) {
                    _deskReservationResult.postValue(DeskReservationResult(error = it.exception))
                }
            }
        }
    }

    private val _startReservationResult = MutableLiveData<StartReservationResult>()
    val startReservationResult: LiveData<StartReservationResult>
        get() = _startReservationResult

    fun startReservation(idReservation: String, authorization: String) {
        startReservationRepository.startReservation(idReservation, authorization)
        startReservationRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _startReservationResult.postValue(
                        StartReservationResult(success = it.data)
                    )
                } else if (it is Result.Error) {
                    _startReservationResult.postValue(StartReservationResult(error = it.exception))
                }
            }
        }
    }

    private val _endReservationResult = MutableLiveData<EndReservationResult>()
    val endReservationResult: LiveData<EndReservationResult>
        get() = _endReservationResult

    fun endReservation(idReservation: String, authorization: String, deskCleaned: Boolean) {
        endReservationRepository.endReservation(idReservation, authorization, deskCleaned)
        endReservationRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _endReservationResult.postValue(
                        EndReservationResult(success = it.data)
                    )
                } else if (it is Result.Error) {
                    _endReservationResult.postValue(EndReservationResult(error = it.exception))
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
}

package com.sweven.blockcovid.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.repositories.*
import com.sweven.blockcovid.services.NetworkClient

/**
 * Factory di provider ViewModel per istanziare RoomViewViewModel.
 * Obbligatorio dato che RoomViewViewModel ha un costruttore non vuoto
 */
class HomeViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                deskStatusRepository = DeskStatusRepository(NetworkClient()),
                deskInfoRepository = DeskInfoRepository(NetworkClient()),
                deskReservationRepository = DeskReservationRepository(NetworkClient()),
                startReservationRepository = StartReservationRepository(NetworkClient()),
                endReservationRepository = EndReservationRepository(NetworkClient()),
                deleteReservationRepository = DeleteReservationRepository(NetworkClient())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

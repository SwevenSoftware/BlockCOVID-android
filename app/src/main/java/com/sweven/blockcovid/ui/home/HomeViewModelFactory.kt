package com.sweven.blockcovid.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.repositories.DeleteReservationRepository
import com.sweven.blockcovid.data.repositories.DeskInfoRepository
import com.sweven.blockcovid.data.repositories.DeskReservationRepository
import com.sweven.blockcovid.data.repositories.DeskStatusRepository
import com.sweven.blockcovid.data.repositories.EndReservationRepository
import com.sweven.blockcovid.data.repositories.StartReservationRepository
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

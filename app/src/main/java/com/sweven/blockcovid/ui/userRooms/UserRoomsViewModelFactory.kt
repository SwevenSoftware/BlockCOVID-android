package com.sweven.blockcovid.ui.userRooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.repositories.UserRoomsRepository
import com.sweven.blockcovid.services.NetworkClient

/**
 * Factory di provider ViewModel per istanziare CleanerRoomsViewModel.
 * Obbligatorio dato che CleanerRoomsdViewModel ha un costruttore non vuoto
 */

class UserRoomsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserRoomsViewModel::class.java)) {
            return UserRoomsViewModel(
                    userRoomsRepository = UserRoomsRepository(NetworkClient())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

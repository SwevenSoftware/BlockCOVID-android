package com.sweven.blockcovid.ui.userRooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.UserRoomsRepository

/**
 * Factory di provider ViewModel per istanziare CleanerRoomsViewModel.
 * Obbligatorio dato che CleanerRoomsdViewModel ha un costruttore non vuoto
 */

class UserRoomsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserRoomsViewModel::class.java)) {
            return UserRoomsViewModel(
                    userRoomsRepository = UserRoomsRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

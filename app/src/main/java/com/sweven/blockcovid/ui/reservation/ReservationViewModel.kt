package com.sweven.blockcovid.ui.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReservationViewModel : ViewModel() {
    private val mutableSelectedItem = MutableLiveData<String>()
    val selectedItem: LiveData<String> get() = mutableSelectedItem

    fun selectItem(item: String) {
        mutableSelectedItem.value = item
    }
}

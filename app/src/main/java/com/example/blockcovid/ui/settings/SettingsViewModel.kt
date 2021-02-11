package com.example.blockcovid.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Wow qui ci sono molte impostazioni!"
    }
    val text: LiveData<String> = _text
}
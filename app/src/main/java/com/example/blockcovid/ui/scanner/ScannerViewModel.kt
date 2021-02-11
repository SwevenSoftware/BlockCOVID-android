package com.example.blockcovid.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ScannerViewModel {
    private val _text = MutableLiveData<String>().apply {
        value = "Bzzz bzzz scannerizzo RFID"
    }
    val text: LiveData<String> = _text
}
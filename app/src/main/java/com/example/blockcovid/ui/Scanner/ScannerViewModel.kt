package com.example.blockcovid.ui.Scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ScannerViewModel {
    private val _text = MutableLiveData<String>().apply {
        value = "This is a scanner Fragment"
    }
    val text: LiveData<String> = _text
}
package com.example.blockcovid.ui.help

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelpViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Ora ti aiuto: questa app... e' un'app"
    }
    val text: LiveData<String> = _text
}
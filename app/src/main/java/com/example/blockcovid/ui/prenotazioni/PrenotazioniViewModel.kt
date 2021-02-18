package com.example.blockcovid.ui.prenotazioni

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrenotazioniViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "prenota pure"
    }
    val text: LiveData<String> = _text

}
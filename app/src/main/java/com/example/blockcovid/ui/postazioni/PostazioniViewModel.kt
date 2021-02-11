package com.example.blockcovid.ui.postazioni

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostazioniViewModel {
    private val _text = MutableLiveData<String>().apply {
        value = "Immagina che qui ci sia una mappa con tutti i posti"
    }
    val text: LiveData<String> = _text
}
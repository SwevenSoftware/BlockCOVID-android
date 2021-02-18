package com.example.blockcovid.ui.prenotazioni

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blockcovid.R

class PrenotazioniFragment : Fragment(){
    private lateinit var prenotazioniViewModel: PrenotazioniViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prenotazioniViewModel =
            ViewModelProvider(this).get(PrenotazioniViewModel::class.java)
        return inflater.inflate(R.layout.fragment_prenotazioni, container, false)
    }


}

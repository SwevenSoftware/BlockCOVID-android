package com.example.blockcovid.ui.prenotazioni

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.blockcovid.R

class PrenotazioniFragment : Fragment(){
    private lateinit var prenotazioniViewModel: PrenotazioniViewModel

    private val args: PrenotazioniFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prenotazioniViewModel =
            ViewModelProvider(this).get(PrenotazioniViewModel::class.java)
        return inflater.inflate(R.layout.fragment_prenotazioni, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tv: TextView = view.findViewById(R.id.idPostazionePrenotata)
        val amount = args.deskId
        tv.text = amount
    }

}

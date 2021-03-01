package com.example.blockcovid.ui.prenotazioni

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.blockcovid.R
import com.example.blockcovid.databinding.ActivityMainBinding

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
        val textDesk: TextView = view.findViewById(R.id.idPostazionePrenotata)
        val textRoom: TextView = view.findViewById(R.id.idStanzaPrenotata)
        val deskId = args.deskId
        val roomId = args.roomId
        textDesk.text = deskId
        textRoom.text = roomId
    }

}

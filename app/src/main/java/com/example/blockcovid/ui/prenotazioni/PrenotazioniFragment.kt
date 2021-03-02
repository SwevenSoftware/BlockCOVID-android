package com.example.blockcovid.ui.prenotazioni

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.blockcovid.R
import java.util.*


class PrenotazioniFragment : Fragment(){
    private lateinit var prenotazioniViewModel: PrenotazioniViewModel

    private val viewModel: PrenotazioniViewModel by activityViewModels()

    var selectedDate = ""

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

        val selezionaData: CalendarView = view.findViewById(R.id.selezionaData)
        selezionaData.setOnDateChangeListener { _, year, month, day ->
            val realmonth = month + 1
            selectedDate = "$year-$realmonth-$day"
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN)
            val date = formatter.parse(selectedDate).time
            val correctDate = formatter.format(Date(date))
            viewModel.selectItem(correctDate)
            println(selectedDate)
            println(correctDate)
        }
    }
}

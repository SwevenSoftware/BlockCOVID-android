package com.sweven.blockcovid.ui.reservation


import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.sweven.blockcovid.R
import java.util.*


class ReservationFragment : Fragment(){
    private lateinit var reservationViewModel: ReservationViewModel

    private val viewModel: ReservationViewModel by activityViewModels()

    var selectedDate = ""

    private val args: ReservationFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        reservationViewModel =
            ViewModelProvider(this).get(ReservationViewModel::class.java)
        return inflater.inflate(R.layout.fragment_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Prende i valori del numero della postazione ed il nome della stanza dal fragment della stanza
        // per poi mostrarli nella TextView
        val textDesk: TextView = view.findViewById(R.id.id_reserved_desk)
        val textRoom: TextView = view.findViewById(R.id.id_reserved_room)
        val deskId = args.deskId
        val roomId = args.roomId
        textDesk.text = deskId
        textRoom.text = roomId

        // Invia la data selezionata sul calendario alla MainActivity per poi poterla inviare al server
        val selectDate: CalendarView = view.findViewById(R.id.select_date)
        selectDate.setOnDateChangeListener { _, year, month, day ->
            val realMonth = month + 1
            selectedDate = "$year-$realMonth-$day"
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN)
            val date = formatter.parse(selectedDate).time
            val correctDate = formatter.format(Date(date))
            viewModel.selectItem(correctDate)
        }
    }
}

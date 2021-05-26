package com.sweven.blockcovid.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.sweven.blockcovid.R
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.TimeZone

class ReservationFragment : Fragment() {
    private lateinit var reservationViewModel: ReservationViewModel

    private val args: ReservationFragmentArgs by navArgs()
    private lateinit var deskX: TextView
    private lateinit var deskY: TextView
    private lateinit var textRoom: TextView
    private lateinit var arrivalTime: TextView
    private lateinit var exitTime: TextView
    private lateinit var selectedDate: TextView
    private lateinit var reserve: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reservationViewModel =
            ViewModelProvider(this, ReservationViewModelFactory()).get(ReservationViewModel::class.java)
        return inflater.inflate(R.layout.fragment_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val arrivalTimeLayout: TextInputLayout = view.findViewById(R.id.arrival_time)
        val exitTimeLayout: TextInputLayout = view.findViewById(R.id.exit_time)
        val selectedDateLayout: TextInputLayout = view.findViewById(R.id.reservation_date)
        reserve = view.findViewById(R.id.reserve_button)

        // Prende i valori del numero della postazione ed il nome della stanza dal fragment della stanza
        // per poi mostrarli nella TextView
        deskX = view.findViewById(R.id.reserved_desk_x)
        deskY = view.findViewById(R.id.reserved_desk_y)
        textRoom = view.findViewById(R.id.id_reserved_room)
        arrivalTime = view.findViewById(R.id.edit_arrival_time)
        exitTime = view.findViewById(R.id.edit_exit_time)
        selectedDate = view.findViewById(R.id.edit_reservation_date)
        val roomId = args.roomId
        deskX.text = args.deskX
        deskY.text = args.deskY
        textRoom.text = roomId
        arrivalTime.text = args.arrival
        exitTime.text = args.exit
        selectedDate.text = args.date

        val nowDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
        val selectedDateTime = LocalDateTime.of(LocalDate.parse(args.date), LocalTime.parse(args.arrival))

        if (selectedDateTime < nowDateTime) {
            reserve.isEnabled = false
            arrivalTimeLayout.error = getString(R.string.reservation_expired)
            exitTimeLayout.error = getString(R.string.reservation_expired)
            selectedDateLayout.error = getString(R.string.reservation_expired)
        }

        val cacheToken = File(context?.cacheDir, "token")
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }

        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
        val mainActivity = viewLifecycleOwner

        reservationViewModel.reservationResult.observe(
            mainActivity,
            {
                checkReservationResult(it, loading)
            }
        )

        val date = selectedDate.text.toString()

        val from = arrivalTime.text.toString()
        val startDateTime = localDateTimeToUTC(date, from)

        val to = exitTime.text.toString()
        val endDateTime = localDateTimeToUTC(date, to)
        reserve.setOnClickListener {
            loading.show()
            reservationViewModel.reserve(deskId = args.deskId, start = startDateTime, end = endDateTime, authorization)
        }
    }

    fun checkReservationResult(formResult: ReservationResult, loading: CircularProgressIndicator) {
        loading.hide()
        if (formResult.success != null) {
            Toast.makeText(context, getString(R.string.reservation_successful), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        } else if (formResult.error != null) {
            showReservationFailed(formResult.error)
            findNavController().navigateUp()
        }
    }

    fun showReservationFailed(errorString: String) {
        Toast.makeText(context, getString(R.string.error).plus(" ").plus(errorString), Toast.LENGTH_SHORT).show()
    }

    fun localDateTimeToUTC(date: String, time: String): String {
        val localDate = LocalDate.parse(date)
        val localTime = LocalTime.parse(time)
        val zonedTimeDate = ZonedDateTime.of(localDate, localTime, TimeZone.getDefault().toZoneId())
        return zonedTimeDate.withZoneSameInstant(ZoneOffset.UTC).toString().dropLast(1)
    }
}

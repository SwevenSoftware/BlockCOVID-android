package com.sweven.blockcovid.ui.editReservation

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sweven.blockcovid.R
import java.io.File
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*

class EditReservationFragment : Fragment() {
    private lateinit var editReservationViewModel: EditReservationViewModel

    private val args: EditReservationFragmentArgs by navArgs()
    private lateinit var localDateTime: LocalDateTime
    private lateinit var textRoom: TextInputEditText
    private lateinit var deskX: TextInputEditText
    private lateinit var deskY: TextInputEditText
    private lateinit var arrivalTime: TextInputEditText
    private lateinit var arrivalTimeLayout: TextInputLayout
    private lateinit var exitTime: TextInputEditText
    private lateinit var exitTimeLayout: TextInputLayout
    private lateinit var selectedDate: TextInputEditText
    private lateinit var selectedDateLayout: TextInputLayout
    private lateinit var openingTime: String
    private lateinit var closingTime: String
    private lateinit var openingDays: Array<String>
    private lateinit var edit: Button
    private lateinit var remove: Button
    private lateinit var loading: CircularProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editReservationViewModel =
            ViewModelProvider(this, EditReservationViewModelFactory()).get(EditReservationViewModel::class.java)
        return inflater.inflate(R.layout.fragment_edit_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Convertitori di formato delle ore e date
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.ITALIAN)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN)

        // Prende i valori del numero della postazione ed il nome della stanza dal fragment della stanza
        // per poi mostrarli nella TextView
        textRoom = view.findViewById(R.id.id_reserved_room)
        deskX = view.findViewById(R.id.reserved_desk_x)
        deskY = view.findViewById(R.id.reserved_desk_y)
        arrivalTime = view.findViewById(R.id.edit_arrival_time)
        arrivalTimeLayout = view.findViewById(R.id.arrival_time)
        exitTime = view.findViewById(R.id.edit_exit_time)
        exitTimeLayout = view.findViewById(R.id.exit_time)
        selectedDate = view.findViewById(R.id.edit_reservation_date)
        selectedDateLayout = view.findViewById(R.id.reservation_date)
        edit = view.findViewById(R.id.edit_reservation_button)
        remove = view.findViewById(R.id.remove_reservation_button)
        textRoom.setText(args.roomId)
        arrivalTime.setText(args.arrival)
        exitTime.setText(args.exit)
        selectedDate.setText(args.date)

        openingTime = ""
        closingTime = ""
        openingDays = Array(0) { "" }

        val nowDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
        val selectedDateTime = LocalDateTime.of(LocalDate.parse(args.date), LocalTime.parse(args.arrival))

        if (selectedDateTime < nowDateTime) {
            edit.isEnabled = false
            arrivalTimeLayout.error = getString(R.string.reservation_expired)
            exitTimeLayout.error = getString(R.string.reservation_expired)
            selectedDateLayout.error = getString(R.string.reservation_expired)
        }

        val cacheToken = File(context?.cacheDir, "token")
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }

        loading = view.findViewById(R.id.loading)
        val mainActivity = viewLifecycleOwner

        val localUTCDateTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES).toString()

        loading.show()
        editReservationViewModel.showRoom(localUTCDateTime, localUTCDateTime, authorization, args.roomId)

        // Funzioni per aprire il TimePicker all'interno di Prenotazioni
        arrivalTime.setOnClickListener {
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTitleText(getString(R.string.select_time_to))
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(LocalTime.now(TimeZone.getDefault().toZoneId()).hour)
                .setMinute(LocalTime.now(TimeZone.getDefault().toZoneId()).minute)
                .build()
            materialTimePicker.addOnPositiveButtonClickListener {
                val newHour: Int = materialTimePicker.hour
                val newMinute: Int = materialTimePicker.minute
                val newTime = "$newHour:$newMinute"
                val time = timeFormatter.parse(newTime).time
                val correctTime = timeFormatter.format(Date(time))
                arrivalTime.setText(correctTime)
            }
            materialTimePicker.show(childFragmentManager, "arrivalTime")
        }

        exitTime.setOnClickListener {
            val materialTimePicker = MaterialTimePicker.Builder()
                .setTitleText(getString(R.string.select_time_from))
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(LocalTime.now(TimeZone.getDefault().toZoneId()).hour)
                .setMinute(LocalTime.now(TimeZone.getDefault().toZoneId()).minute)
                .build()
            materialTimePicker.addOnPositiveButtonClickListener {
                val newHour: Int = materialTimePicker.hour
                val newMinute: Int = materialTimePicker.minute
                val newTime = "$newHour:$newMinute"
                val time = timeFormatter.parse(newTime).time
                val correctTime = timeFormatter.format(Date(time))
                exitTime.setText(correctTime)
            }
            materialTimePicker.show(childFragmentManager, "exitTime")
        }

        // Invia la data selezionata sul calendario alla MainActivity per poi poterla inviare al server
        selectedDate.setOnClickListener {
            val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_day))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            materialDatePicker.addOnPositiveButtonClickListener {
                val selectDate = materialDatePicker.selection
                val correctDate = dateFormatter.format(selectDate)
                selectedDate.setText(correctDate)
            }
            materialDatePicker.show(childFragmentManager, "reservationDate")
        }

        arrivalTime.afterTextChanged {
            localDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
            editReservationViewModel.inputDataChanged(
                localDateTime,
                arrivalTime.text.toString(),
                exitTime.text.toString(),
                selectedDate.text.toString(),
                openingTime,
                closingTime,
                openingDays
            )
        }
        exitTime.afterTextChanged {
            localDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
            editReservationViewModel.inputDataChanged(
                localDateTime,
                arrivalTime.text.toString(),
                exitTime.text.toString(),
                selectedDate.text.toString(),
                openingTime,
                closingTime,
                openingDays
            )
        }
        selectedDate.afterTextChanged {
            localDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
            editReservationViewModel.inputDataChanged(
                localDateTime,
                arrivalTime.text.toString(),
                exitTime.text.toString(),
                selectedDate.text.toString(),
                openingTime,
                closingTime,
                openingDays
            )
        }

        editReservationViewModel.editReservationResult.observe(
            mainActivity,
            {
                checkEditReservationResult(it)
            }
        )

        editReservationViewModel.deleteReservationResult.observe(
            mainActivity,
            {
                checkDeleteReservationResult(it)
            }
        )

        editReservationViewModel.roomViewResult.observe(
            mainActivity,
            {
                checkRoomViewResult(it)
            }
        )

        editReservationViewModel.editReservationForm.observe(
            mainActivity,
            Observer {
                val reservationState = it ?: return@Observer

                if (reservationState.arrivalTimeError != null) {
                    arrivalTimeLayout.error = getString(reservationState.arrivalTimeError)
                } else {
                    arrivalTimeLayout.error = null
                }
                if (reservationState.exitTimeError != null) {
                    exitTimeLayout.error = getString(reservationState.exitTimeError)
                } else {
                    exitTimeLayout.error = null
                }
                if (reservationState.selectedDateError != null) {
                    selectedDateLayout.error = getString(reservationState.selectedDateError)
                } else {
                    selectedDateLayout.error = null
                }
                edit.isEnabled = reservationState.isDataValid
            }
        )

        edit.setOnClickListener {
            val date = selectedDate.text.toString()

            val from = arrivalTime.text.toString()
            val startDateTime = localDateTimeToUTC(date, from)

            val to = exitTime.text.toString()
            val endDateTime = localDateTimeToUTC(date, to)
            loading.show()
            editReservationViewModel.editReservation(
                idReservation = args.reservationId, deskId = args.deskId, start = startDateTime,
                end = endDateTime, authorization = authorization
            )
        }

        remove.setOnClickListener {
            loading.show()
            editReservationViewModel.deleteReservation(
                idReservation = args.reservationId, authorization = authorization
            )
        }
    }

    fun checkEditReservationResult(formResult: EditReservationResult) {
        loading.hide()
        if (formResult.success != null) {
            Toast.makeText(context, getString(R.string.reservation_edited), Toast.LENGTH_SHORT).show()
            findNavController().popBackStack(R.id.navigation_user_account, false)
        } else if (formResult.error != null) {
            showReservationFailed(formResult.error)
            findNavController().popBackStack(R.id.navigation_user_account, false)
        }
    }

    fun checkDeleteReservationResult(formResult: DeleteReservationResult) {
        loading.hide()
        if (formResult.success != null) {
            Toast.makeText(context, getString(R.string.reservation_deleted), Toast.LENGTH_SHORT).show()

            // remove cache files for checking the reservation id/end time
            val cacheReservationId = File(context?.cacheDir, "reservationId")
            val reservationEndTime = File(context?.cacheDir, "reservationEndTime")
            if (cacheReservationId.exists() && cacheReservationId.readText() == args.reservationId) {
                if (reservationEndTime.exists()) {
                    reservationEndTime.delete()
                }
                cacheReservationId.delete()
            }
            findNavController().popBackStack(R.id.navigation_user_account, false)
        } else if (formResult.error != null) {
            showReservationFailed(formResult.error)
            findNavController().popBackStack(R.id.navigation_user_account, false)
        }
    }

    fun checkRoomViewResult(formResult: RoomViewResult) {
        loading.hide()
        if (formResult.success != null) {
            openingTime = formResult.success.openingTime!!
            closingTime = formResult.success.closingTime!!
            openingDays = formResult.success.openingDays!!
            val idArray = formResult.success.idArray!!
            val xArray = formResult.success.xArray!!
            val yArray = formResult.success.yArray!!

            for (i in formResult.success.idArray.indices) {
                if (idArray[i] == args.deskId) {
                    deskX.setText((xArray[i] + 1).toString())
                    deskY.setText((yArray[i] + 1).toString())
                    edit.isEnabled = true
                    localDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
                    editReservationViewModel.inputDataChanged(
                        localDateTime,
                        arrivalTime.text.toString(),
                        exitTime.text.toString(),
                        selectedDate.text.toString(),
                        openingTime,
                        closingTime,
                        openingDays
                    )
                    break
                }
            }
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

    private fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}

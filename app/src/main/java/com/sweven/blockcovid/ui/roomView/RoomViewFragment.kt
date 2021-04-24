package com.sweven.blockcovid.ui.roomView

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sweven.blockcovid.R
import java.io.File
import java.util.*
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import java.time.*
import java.time.temporal.ChronoUnit


class RoomViewFragment : Fragment(){

    private lateinit var roomViewViewModel: RoomViewViewModel
    private val args: RoomViewFragmentArgs by navArgs()
    private lateinit var arrivalTime: TextInputEditText
    private lateinit var exitTime: TextInputEditText
    private lateinit var selectDate: TextInputEditText
    private lateinit var arrivalTimeLayout: TextInputLayout
    private lateinit var exitTimeLayout: TextInputLayout
    private lateinit var selectDateLayout: TextInputLayout
    private lateinit var layout: ConstraintLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        roomViewViewModel =
                ViewModelProvider(this, RoomViewViewModelFactory()).get(RoomViewViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_room_view, container, false)
        val activity: AppCompatActivity = activity as AppCompatActivity
        val actionBar = activity.supportActionBar
        actionBar?.title = args.roomName
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
        val cacheTheme = File(context?.cacheDir, "theme")
        val cacheToken = File(context?.cacheDir, "token")
        val darkTheme = cacheTheme.readText() == "1"
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }
        val mainActivity = viewLifecycleOwner
        val openingTime = args.openingTime
        val closingTime = args.closingTime
        val daysOpen = args.daysOpen

        // Convertitori di formato delle ore e date
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.ITALIAN)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN)

        arrivalTime = view.findViewById(R.id.edit_arrival_time)
        exitTime = view.findViewById(R.id.edit_exit_time)
        selectDate = view.findViewById(R.id.edit_reservation_date)
        arrivalTimeLayout = view.findViewById(R.id.arrival_time)
        exitTimeLayout = view.findViewById(R.id.exit_time)
        selectDateLayout = view.findViewById(R.id.reservation_date)
        layout = view.findViewById(R.id.desk_layout)

        // Imposta il tempo e la data sui selettori in alto
        arrivalTime.setText(LocalTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES).toString())
        exitTime.setText(LocalTime.now(TimeZone.getDefault().toZoneId()).plusHours(1).truncatedTo(ChronoUnit.MINUTES).toString())
        selectDate.setText(LocalDate.now(TimeZone.getDefault().toZoneId()).toString())
        var localDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
        roomViewViewModel.inputDataChanged(
            localDateTime,
            arrivalTime.text.toString(),
            exitTime.text.toString(),
            selectDate.text.toString(),
            openingTime,
            closingTime,
            daysOpen
        )

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
        selectDate.setOnClickListener {
            val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_day))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            materialDatePicker.addOnPositiveButtonClickListener {
                val selectedDate = materialDatePicker.selection
                val correctDate = dateFormatter.format(selectedDate)
                selectDate.setText(correctDate)
            }
            materialDatePicker.show(childFragmentManager, "reservationDate")
        }


        arrivalTime.afterTextChanged {
            localDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
            roomViewViewModel.inputDataChanged(
                localDateTime,
                arrivalTime.text.toString(),
                exitTime.text.toString(),
                selectDate.text.toString(),
                openingTime,
                closingTime,
                daysOpen
            )
        }
        exitTime.afterTextChanged {
            localDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
            roomViewViewModel.inputDataChanged(
                localDateTime,
                arrivalTime.text.toString(),
                exitTime.text.toString(),
                selectDate.text.toString(),
                openingTime,
                closingTime,
                daysOpen
            )
        }
        selectDate.afterTextChanged {
            localDateTime = LocalDateTime.now(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.MINUTES)
            roomViewViewModel.inputDataChanged(
                localDateTime,
                arrivalTime.text.toString(),
                exitTime.text.toString(),
                selectDate.text.toString(),
                openingTime,
                closingTime,
                daysOpen
            )
        }

        roomViewViewModel.roomViewResult.observe(mainActivity, {
            createRoomViewResult(it, loading, darkTheme)
        })

        roomViewViewModel.roomViewForm.observe(mainActivity, Observer {
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
                selectDateLayout.error = getString(reservationState.selectedDateError)
            } else {
                selectDateLayout.error = null
            }

            if(reservationState.isDataValid) {
                loading.show()
                val date = selectDate.text.toString()

                val from = arrivalTime.text.toString()
                val startDateTime = localDateTimeToUTC(date, from)

                val to = exitTime.text.toString()
                val endDateTime = localDateTimeToUTC(date, to)

                roomViewViewModel.showRoom(startDateTime, endDateTime, authorization, args.roomName)
            } else {
                layout.removeAllViews()
            }
        })
    }

    fun createRoomViewResult(roomDesks: RoomViewResult, loading: CircularProgressIndicator, theme: Boolean) {
        loading.hide()

        if (roomDesks.success != null) {
            val idArray = roomDesks.success.idArray
            val xArray = roomDesks.success.xArray
            val yArray = roomDesks.success.yArray
            val availableArray = roomDesks.success.availableArray
            layout.removeAllViews()
            if (idArray != null && xArray != null && yArray != null && availableArray != null) {
                if (idArray.isNotEmpty()) {
                    for (i in idArray.indices) {
                        val imgButton = Button(context)
                        imgButton.id = i+1
                        imgButton.width = 50
                        imgButton.height = 50
                        if (availableArray[i]) {
                            imgButton.setOnClickListener {
                                val action = RoomViewFragmentDirections.actionNavigationRoomViewToNavigationReservation(
                                    (xArray[i] + 1).toString(), (yArray[i] + 1).toString(),
                                    arrivalTime.text.toString(), exitTime.text.toString(),
                                    selectDate.text.toString(), args.roomName, idArray[i]
                                )
                                findNavController().navigate(action)
                            }
                            if (theme) {
                                imgButton.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.green_desk_night,
                                    null
                                )
                            } else {
                                imgButton.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.green_desk,
                                    null
                                )
                            }
                        } else {
                            imgButton.isClickable = false
                            if (theme) {
                                imgButton.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.red_desk_night,
                                    null
                                )
                            } else {
                                imgButton.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.red_desk,
                                    null
                                )
                            }
                        }
                        layout.addView(imgButton)
                        val params = imgButton.layoutParams as ConstraintLayout.LayoutParams
                        params.leftToLeft = ConstraintSet.PARENT_ID
                        params.leftMargin = xArray[i] * 500
                        params.topToTop = ConstraintSet.PARENT_ID
                        params.topMargin = yArray[i] * 500
                        imgButton.layoutParams = params
                    }
                } else {
                    showDesksFailed(getString(R.string.room_empty))
                }
            }
        } else if (roomDesks.error != null) {
            showDesksFailed(roomDesks.error)
        }
    }

    fun showDesksFailed(errorString: String){
        Toast.makeText(context,getString(R.string.error).plus(" ").plus(errorString),Toast.LENGTH_SHORT).show()
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
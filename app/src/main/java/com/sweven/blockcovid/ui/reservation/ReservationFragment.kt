package com.sweven.blockcovid.ui.reservation


import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sweven.blockcovid.R
import com.sweven.blockcovid.services.APIReserve
import com.sweven.blockcovid.services.NetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.SocketTimeoutException
import java.time.LocalTime
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

        // Convertitori di formato delle ore e date
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.ITALIAN)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN)

        // Funzioni per aprire il TimePicker all'interno di Prenotazioni
        val arrivalTime: TextInputEditText = view.findViewById(R.id.edit_arrival_time)
        val exitTime: TextInputEditText = view.findViewById(R.id.edit_exit_time)

        arrivalTime.setOnClickListener {
            val materialTimePicker = MaterialTimePicker.Builder()
                    .setTitleText(getString(R.string.select_time_from))
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(LocalTime.now().hour)
                    .setMinute(LocalTime.now().minute)
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
                .setTitleText(getString(R.string.select_time_to))
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(LocalTime.now().hour)
                .setMinute(LocalTime.now().minute)
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
        val selectDate: TextInputEditText = view.findViewById(R.id.edit_reservation_date)

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


        // Funzione per controllare che i selettori non siano vuoti
        val arrivalTimeLayout: TextInputLayout = view.findViewById(R.id.arrival_time)
        val exitTimeLayout: TextInputLayout = view.findViewById(R.id.exit_time)
        val selectDateLayout: TextInputLayout = view.findViewById(R.id.reservation_date)
        val reserveButton: Button = view.findViewById(R.id.reserve_button)
        val mainActivity = viewLifecycleOwner
        reservationViewModel.reservationFormState.observe(mainActivity, Observer {
            val reservationState = it ?: return@Observer

            reserveButton.isEnabled = reservationState.isDataValid

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
        })

        arrivalTime.afterTextChanged {
            reservationViewModel.inputDataChanged(
                    arrivalTime.text.toString(),
                    exitTime.text.toString(),
                    selectDate.text.toString(),
            )
        }
        exitTime.afterTextChanged {
            reservationViewModel.inputDataChanged(
                    arrivalTime.text.toString(),
                    exitTime.text.toString(),
                    selectDate.text.toString(),
            )
        }
        selectDate.afterTextChanged {
            reservationViewModel.inputDataChanged(
                    arrivalTime.text.toString(),
                    exitTime.text.toString(),
                    selectDate.text.toString(),
            )
        }

        // Funzione per inviare la richiesta POST al server per prenotare una postazione
        reserveButton.setOnClickListener {
            val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
            loading.show()

            val nameRoom = textRoom.text.toString()
            val idDesk = textDesk.text.toString().toInt()
            val date = selectDate.text.toString()
            val from = arrivalTime.text.toString()
            val to = exitTime.text.toString()
            val cacheToken = File(context?.cacheDir, "token")
            var authorization = ""
            if (cacheToken.exists()) {
                authorization = cacheToken.readText()
            }
            val retrofit = NetworkClient.retrofitClient

            val service = retrofit.create(APIReserve::class.java)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response =
                            service.deskReserve(nameRoom, idDesk, date, from, to, authorization)
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            if(response.errorBody()==null) {
                                val gson = GsonBuilder().setPrettyPrinting().create()
                                val responseJson =
                                        gson.toJson(JsonParser.parseString(response.body()?.string()))
                                print("Response: ")
                                println(responseJson)
                                loading.hide()
                                Toast.makeText(
                                        context,
                                        getString(R.string.reservation_successful),
                                        Toast.LENGTH_LONG
                                ).show()
                            } else {
                                activity?.runOnUiThread {
                                    loading.hide()
                                    Toast.makeText(
                                            context,
                                            response.errorBody()?.string().toString(),
                                            Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else {
                        activity?.runOnUiThread {
                            loading.hide()
                            Toast.makeText(
                                    context,
                                    response.errorBody()?.string().toString(),
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (exception: SocketTimeoutException) {
                    activity?.runOnUiThread {
                        loading.hide()
                        Toast.makeText(
                                context,
                                getString(R.string.timeout),
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
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

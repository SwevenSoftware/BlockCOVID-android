package com.sweven.blockcovid

import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.sweven.blockcovid.databinding.ActivityMainBinding
import com.sweven.blockcovid.services.APIReserve
import com.sweven.blockcovid.ui.prenotazioni.PrenotazioniViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sweven.blockcovid.services.NetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    // Controllo dello stato dell'adattatore NFC
    private var nfcAdapter: NfcAdapter? = null

    // Lettura NFC tags mentre l'applicazione e' attiva in primo piano
    private var nfcPendingIntent: PendingIntent? = null

    private var logText = "logText: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_help, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Controlla se NFC è supportato e abilitato
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        logMessage("NFC Adapter", nfcAdapter.toString())
        logMessage("NFC supported", (nfcAdapter != null).toString())
        logMessage("NFC enabled", (nfcAdapter?.isEnabled).toString())


        // Leggi tutti i tag quando l'app è in esecuzione e in primo piano.
        // Crea un PendingIntent generico che verrà consegnato a questa attività. Lo stack NFC
        // riempirà l'Intent con i dettagli del tag scoperto prima di
        // consegnarlo a questa attività.
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        if (intent != null) {
            // Controlla se l'app è stata avviata tramite un intento NFC
            logMessage("Found intent in onCreate", intent.action.toString())
            processIntent(intent)
        }
    }

    // Funzione per far apparire il pulsante Profilo in alto a destra
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    // Funzione per linkare la pagina di Account tramite il pulsante in alto a destra dello schermo
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.navigation_login) {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_navigation_account)
        }
        return super.onOptionsItemSelected(item)
    }

    // Funzione per far funzionare il bottone per tornare indietro situato in alto a sinistra dello schermo
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Funzione per fare il logout, elimina il file token dalla cache
    fun logout(view: View) {
        val context = applicationContext
        val cacheFile = File(context.cacheDir, "token")
        if(cacheFile.exists()) {
            cacheFile.delete()
            view.findNavController().navigate(R.id.action_global_navigation_login)
        }
    }

    // Funzione per navigare da Home a Scanner (bottone Scanner)
    fun goScanner(view: View) {
        view.findNavController().navigate(R.id.action_navigation_home_to_navigation_scanner)
    }

    // Funzione per navigare da Home a Stanza1 (bottone Postazioni)
    fun goPostazioni(view: View) {
        view.findNavController().navigate(R.id.action_navigation_home_to_navigation_stanza1)
    }

    // Funzione per navigare al fragment Stanza1 (globale)
    fun goStanza1(view: View) {
        view.findNavController().navigate(R.id.action_global_navigation_stanza1)
    }

    // Funzione per navigare al fragment Stanza2 (globale)
    fun goStanza2(view: View) {
        view.findNavController().navigate(R.id.action_global_navigation_stanza2)
    }

    // Funzione per inviare la richiesta POST al server per prenotare una postazione
    fun prenota(view: View) {
        val nameRoom = findViewById<TextView>(R.id.idStanzaPrenotata).text.toString()
        val idDesk = findViewById<TextView>(R.id.idPostazionePrenotata).text.toString().toInt()
        var date = ""
        val viewModel: PrenotazioniViewModel by viewModels()
        viewModel.selectedItem.observe(this, Observer { item ->
            date = item
        })
        val from = findViewById<TextView>(R.id.editOrarioArrivo).text.toString()
        val to = findViewById<TextView>(R.id.editOrarioUscita).text.toString()
        val context = applicationContext
        val cacheFile = File(context.cacheDir, "token")
        var authorization = ""
        if(cacheFile.exists()) {
            authorization = cacheFile.readText()
        }

        val retrofit = NetworkClient.retrofitClient

        val service = retrofit.create(APIReserve::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.deskReserve(nameRoom, idDesk, date, from, to, authorization)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val responseJson = gson.toJson(JsonParser.parseString(response.body()?.string()))
                    print("Response: ")
                    println(responseJson)
                    Toast.makeText(
                            applicationContext,
                            "Postazione prenotata",
                            Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        "Prenotazione fallita",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // Funzione per navigare al fragment Postazioni (globale)
    fun goPrenotazioni(view: View) {
        val deskId = view.contentDescription.toString()
        val roomId = view.tag.toString()
        val action = MobileNavigationDirections.actionGlobalNavigationPrenotazioni(deskId, roomId)
        view.findNavController().navigate(action)
    }

    // Funzione per aggiornare il log dei messaggi NFC letti
    fun refreshLogs(view: View) {
        println(logText)
        val tvMessages = findViewById<TextView>(R.id.tv_messages)
        val svMessages = findViewById<ScrollView>(R.id.sv_messages)
        tvMessages.text = logText
        svMessages.post {
            svMessages.smoothScrollTo(0, svMessages.bottom)
        }
    }

    // Funzione per aprire il TimePicker all'interno di Prenotazioni
    fun openTimePicker(view: View) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            when (view.id) {
                R.id.editOrarioArrivo -> {
                    val orarioArrivo = findViewById<TextView>(R.id.editOrarioArrivo)
                    orarioArrivo.text = SimpleDateFormat("HH:mm", Locale.ITALIAN).format(cal.time)
                }
                R.id.editOrarioUscita -> {
                    val orarioUscita = findViewById<TextView>(R.id.editOrarioUscita)
                    orarioUscita.text = SimpleDateFormat("HH:mm", Locale.ITALIAN).format(cal.time)
                }
            }
        }
        TimePickerDialog(this, 2, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    override fun onResume() {
        super.onResume()
        // Ottieni tutti gli intenti scoperti NFC
        // Si assicura che l'app riceva tutti i messaggi NFC rilevati fintanto che è in primo piano.
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        // Disabilita l'invio in primo piano quando questa attività non è più in primo piano
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logMessage("Found intent in onNewIntent", intent?.action.toString())
        // Se abbiamo un intento mentre l'app è in esecuzione, controlla anche se si tratta di un nuovo messaggio NDEF
        // che è stato scoperto
        if (intent != null) processIntent(intent)
    }

    /**
     * Controlla se l'Intent ha l'azione "ACTION_NDEF_DISCOVERED". Se sì, viene gestito
     * di conseguenza e si analizzano i messaggi NFC.
     */

    private fun processIntent(checkIntent: Intent) {
        // Controlla se l'Intent ha l'azione di un tag NFC scoperto
        // con contenuti formattati NDEF
        if (checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            logMessage("New NDEF intent", checkIntent.toString())

            // Recupera il messaggio NDEF grezzo dal tag
            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMessages != null) {
                logMessage("Raw messages", rawMessages.size.toString())
            }

            if (rawMessages != null) {
                val messages = arrayOfNulls<NdefMessage?>(rawMessages.size)
                for (i in rawMessages.indices) {
                    messages[i] = rawMessages[i] as NdefMessage
                }
                // Elabora l'array di messaggi.
                processNdefMessages(messages)
            }
        }
    }

    /**
     * Analizza il contenuto del messaggio NDEF e lo stampa nel log.
     */
    private fun processNdefMessages(ndefMessages: Array<NdefMessage?>) {
        // Passa attraverso tutti i messaggi NDEF trovati sul tag NFC
        for (curMsg in ndefMessages) {
            if (curMsg != null) {
                // Stampa informazioni generiche sul messaggio NDEF
                logMessage("Message", curMsg.toString())
                logMessage("Records", curMsg.records.size.toString())

                // Scorri tutti i record contenuti nel messaggio
                for (curRecord in curMsg.records) {
                    if (curRecord.toUri() != null) {
                        // URI NDEF Tag
                        logMessage("- URI", curRecord.toUri().toString())
                    } else {
                        // Altri tag NDEF: stampa semplicemente il payload
                        // il drop(3) serve ad eliminare i primi caratteri di default dal content
                        logMessage("- Contents", curRecord.payload.decodeToString().drop(3))
                    }
                }
            }
        }
    }

    /**
     * Registra un messaggio nella visualizzazione del testo di debug.
     * @param header testo del titolo del messaggio, stampato in grassetto
     * @param text parametro facoltativo contenente i dettagli sul messaggio. Stampato in testo normale.
     */
    private fun logMessage(header: String, text: String?) {
        logText = logText.plus(if (text.isNullOrBlank())
            fromHtml("<b>$header</b><br>")
        else
            fromHtml("<b>$header</b>: $text<br>"))
    }

    /**
     * Converte stringhe formattate HTML in testo con spanning (con stile), per l'inserimento in TextView.
     * Esternalizzato in una funzione propria poiché il metodo fromHtml (html) è stato deprecato
     * con Android N. Questo metodo sceglie la variante giusta a seconda del sistema operativo.
     * @param html stringa in formato HTML da convertire in un testo con spanning.
     */

    private fun fromHtml(html: String): Spanned {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
    }
}

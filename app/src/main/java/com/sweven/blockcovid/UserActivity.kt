package com.sweven.blockcovid

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sweven.blockcovid.ui.home.HomeFragmentDirections


class UserActivity : AppCompatActivity() {

    // Controllo dello stato dell'adattatore NFC
    private var nfcAdapter: NfcAdapter? = null

    // Lettura NFC tags mentre l'applicazione e' attiva in primo piano
    private var nfcPendingIntent: PendingIntent? = null

    private var logText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_user)
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_user_rooms, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Controlla se NFC è supportato e abilitato
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Leggi tutti i tag quando l'app è in esecuzione e in primo piano.
        // Crea un PendingIntent generico che verrà consegnato a questa attività. Lo stack NFC
        // riempirà l'Intent con i dettagli del tag scoperto prima di
        // consegnarlo a questa attività.
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        if (intent != null) {
            // Controlla se l'app è stata avviata tramite un intento NFC
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
            findNavController(R.id.nav_host_fragment_user).navigate(R.id.action_global_navigation_user_account)
        }
        return super.onOptionsItemSelected(item)
    }

    // Funzione per far funzionare il bottone per tornare indietro situato in alto a sinistra dello schermo
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_user)
        return navController.navigateUp() || super.onSupportNavigateUp()
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
        // Se abbiamo un intento mentre l'app è in esecuzione, controlla anche se si tratta di un nuovo messaggio NDEF
        // che è stato scoperto
        if (nfcAdapter?.isEnabled == true) {
            if (intent != null) processIntent(intent)
        } else {
            Toast.makeText(this, getString(R.string.error).plus(" ").plus(R.string.nfc_disabled), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Controlla se l'Intent ha l'azione "ACTION_NDEF_DISCOVERED". Se sì, viene gestito
     * di conseguenza e si analizzano i messaggi NFC.
     */

    private fun processIntent(checkIntent: Intent) {
        // Controlla se l'Intent ha l'azione di un tag NFC scoperto
        // con contenuti formattati NDEF
        if (checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            // Recupera il messaggio NDEF grezzo dal tag
            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

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
                // Scorri tutti i record contenuti nel messaggio
                for (curRecord in curMsg.records) {
                    logText = curRecord.payload.decodeToString().drop(3)

                    val action = HomeFragmentDirections.actionGlobalNavigationHome(logText)
                    findNavController(R.id.nav_host_fragment_user).navigate(action)
                }
            }
        }
    }
}

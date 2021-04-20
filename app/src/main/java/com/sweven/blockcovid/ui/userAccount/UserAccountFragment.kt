package com.sweven.blockcovid.ui.userAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sweven.blockcovid.R
import java.io.File

class UserAccountFragment: Fragment() {

    private lateinit var userAccountViewModel: UserAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userAccountViewModel =
            ViewModelProvider(this).get(UserAccountViewModel::class.java)
        return inflater.inflate(R.layout.fragment_user_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cacheToken = File(context?.cacheDir, "token")
        val cacheExpiry = File(context?.cacheDir, "expiryDate")
        val cacheUser = File(context?.cacheDir, "username")
        val cacheAuth = File(context?.cacheDir, "authority")
        val showUser= view.findViewById<TextView>(R.id.showUsername)
        val myReservationsButton = view.findViewById<Button>(R.id.my_reservations_button)
        val changePasswordButton = view.findViewById<Button>(R.id.change_password_button)
        val logoutButton = view.findViewById<Button>(R.id.logout_button)

        //Funzione per mostrare il messaggio di benvenuto user
       showUserFun(showUser,cacheUser)
        // Funzione per navigare da Account a MyReservations (bottone My Reservations)
        myReservationsButtonFun(myReservationsButton)
        // Funzione per navigare da Account a ChangePassword (bottone Change Password)
       changePasswordButtonFun(changePasswordButton)
        // Funzione per fare il logout, elimina il file token dalla cache
        logoutClearToken(logoutButton,cacheToken,cacheExpiry,cacheUser,cacheAuth)
    }

    private fun showUserFun(showUser:TextView,cacheUser:File) {
    if(cacheUser.exists()) {
         showUser.text = getString(R.string.welcome).plus(" ").plus(cacheUser.readText())
       }
    }

    private fun changePasswordButtonFun(changePasswordButton: Button){
        changePasswordButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_user_account_to_navigation_change_password)
        }
    }

    private fun myReservationsButtonFun(myReservationsButton: Button){
        myReservationsButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_user_account_to_navigation_reservation_view)
        }
    }

    private fun logoutClearToken(logoutButton:Button, cacheToken:File, cacheExpiry:File, cacheUser:File, cacheAuth:File) {
        logoutButton.setOnClickListener {
            if (cacheToken.exists()) {
                cacheToken.delete()
                cacheExpiry.delete()
                cacheUser.delete()
                cacheAuth.delete()
                view?.findNavController()
                    ?.navigate(R.id.action_global_navigation_login)
            }
        }
    }
}

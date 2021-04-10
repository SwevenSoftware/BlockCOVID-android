package com.sweven.blockcovid.ui.changePassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sweven.blockcovid.R
import com.sweven.blockcovid.services.APIChangePassword
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.ui.login.LoginViewModel
import com.sweven.blockcovid.ui.login.LoginViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class ChangePasswordFragment : Fragment() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    private var netClient = NetworkClient()

    fun setNetwork(nc: NetworkClient) {
        netClient = nc
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        changePasswordViewModel =
                ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editOldPassword: TextInputEditText = view.findViewById(R.id.edit_old_password)
        val editNewPassword: TextInputEditText = view.findViewById(R.id.edit_new_password)
        val editRepeatPassword: TextInputEditText = view.findViewById(R.id.edit_repeat_password)
        val oldPassword = view.findViewById<TextInputLayout>(R.id.old_password)
        val newPassword = view.findViewById<TextInputLayout>(R.id.new_password)
        val repeatPassword = view.findViewById<TextInputLayout>(R.id.repeat_password)
        val changePassword: Button = view.findViewById(R.id.change_password_button)
        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)

        val mainActivity = viewLifecycleOwner

        changePasswordViewModel.changePasswordFormState.observe(mainActivity, Observer {
            checkPasswordState(changePassword, it, oldPassword, newPassword, repeatPassword)
        })

        changePasswordViewModel.changePasswordResult.observe(mainActivity, Observer {
            checkPasswordResult(it, loading,editOldPassword,editNewPassword,editRepeatPassword)
        })

        editOldPassword.afterTextChanged {
            changePasswordViewModel.inputDataChanged(
                editOldPassword.text.toString(),
                editNewPassword.text.toString(),
                editRepeatPassword.text.toString(),
            )
        }
        editNewPassword.afterTextChanged {
            changePasswordViewModel.inputDataChanged(
                editOldPassword.text.toString(),
                editNewPassword.text.toString(),
                editRepeatPassword.text.toString(),
            )
        }
        editRepeatPassword.afterTextChanged {
            changePasswordViewModel.inputDataChanged(
                editOldPassword.text.toString(),
                editNewPassword.text.toString(),
                editRepeatPassword.text.toString(),
            )
        }

        // Funzione per fare la richiesta di cambio password al server
        changePassword.setOnClickListener {
            loading.show()
            val oldPasswordText = editOldPassword.text.toString()
            val newPasswordText = editNewPassword.text.toString()
            val cacheToken = File(context?.cacheDir, "token")
            var authorization = ""
            if (cacheToken.exists()) {
                authorization = cacheToken.readText()
            }
            changePasswordViewModel.changePasswordRepo(oldPasswordText,newPasswordText,authorization)
        }
    }
    fun checkPasswordState(changePassword:Button,changePasswordState:ChangePasswordFormState,oldPassword:TextInputLayout,newPassword:TextInputLayout
    ,repeatPassword:TextInputLayout) {

        changePassword.isEnabled = changePasswordState.isDataValid

        if (changePasswordState.oldPasswordError != null) {
            oldPassword.error = getString(changePasswordState.oldPasswordError)
        } else {
            oldPassword.error = null
        }
        if (changePasswordState.newPasswordError != null) {
            newPassword.error = getString(changePasswordState.newPasswordError)
        } else {
            newPassword.error = null
        }
        if (changePasswordState.repeatPasswordError != null) {
            repeatPassword.error = getString(changePasswordState.repeatPasswordError)
        } else {
            repeatPassword.error = null
        }
    }

    fun checkPasswordResult(formResult: ChangePasswordResult,loading:CircularProgressIndicator,
                            oldPassword: TextInputEditText,newPassword: TextInputEditText,repeatPassword: TextInputEditText){
        loading.hide()
        if(formResult.error!=null){
            oldPassword.text?.clear()
            newPassword.text?.clear()
            repeatPassword.text?.clear()
            showChangePasswordFailed(formResult.error)
        }
        else{
            view?.findNavController()?.navigate(R.id.action_navigation_change_password_to_navigation_account)
        }
    }

    private fun showChangePasswordFailed(errorString: String){
        Toast.makeText(context,"Error".plus(" ").plus(errorString),Toast.LENGTH_SHORT).show()
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



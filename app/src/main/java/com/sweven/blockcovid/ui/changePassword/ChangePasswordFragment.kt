package com.sweven.blockcovid.ui.changePassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sweven.blockcovid.R
import com.sweven.blockcovid.services.APIChangePassword
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.net.SocketTimeoutException

class ChangePasswordFragment : Fragment() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel

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
        val oldPassword: EditText = view.findViewById(R.id.edit_old_password)
        val newPassword: EditText = view.findViewById(R.id.edit_new_password)
        val repeatPassword: EditText = view.findViewById(R.id.edit_repeat_password)
        val changePassword: Button = view.findViewById(R.id.change_password_button)

        val mainActivity = viewLifecycleOwner
        changePasswordViewModel.changePasswordFormState.observe(mainActivity, Observer {
            val changePasswordState = it ?: return@Observer

            changePassword.isEnabled = changePasswordState.isDataValid

            if (changePasswordState.oldPasswordError != null) {
                oldPassword.error = getString(changePasswordState.oldPasswordError)
            }
            if (changePasswordState.newPasswordError != null) {
                newPassword.error = getString(changePasswordState.newPasswordError)
            }
            if (changePasswordState.repeatPasswordError != null) {
                repeatPassword.error = getString(changePasswordState.repeatPasswordError)
            }
        })

        oldPassword.afterTextChanged {
            changePasswordViewModel.inputDataChanged(
                oldPassword.text.toString(),
                newPassword.text.toString(),
                repeatPassword.text.toString(),
            )
        }
        newPassword.afterTextChanged {
            changePasswordViewModel.inputDataChanged(
                oldPassword.text.toString(),
                newPassword.text.toString(),
                repeatPassword.text.toString(),
            )
        }
        repeatPassword.afterTextChanged {
            changePasswordViewModel.inputDataChanged(
                oldPassword.text.toString(),
                newPassword.text.toString(),
                repeatPassword.text.toString(),
            )
        }

        // Funzione per fare la richiesta di cambio password al server
        changePassword.setOnClickListener {
            val retrofit = NetworkClient.retrofitClient
            val service = retrofit.create(APIChangePassword::class.java)

            val oldPasswordText = oldPassword.text.toString()
            val newPasswordText = newPassword.text.toString()

            val cacheToken = File(context?.cacheDir, "token")
            var authorization = ""
            if (cacheToken.exists()) {
                authorization = cacheToken.readText()
            }

            val jsonObject = JSONObject()
            jsonObject.put("old_password", oldPasswordText)
            jsonObject.put("new_password", newPasswordText)

            val jsonObjectString = jsonObject.toString()
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response =
                        service.changePassword(authorization, requestBody)
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            if (response.errorBody() == null) {
                                val gson = GsonBuilder().setPrettyPrinting().create()
                                val responseJson =
                                    gson.toJson(JsonParser.parseString(response.body()?.string()))
                                print("Response: ")
                                println(responseJson)
                                activity?.runOnUiThread {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.password_changed),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                view.findNavController().navigate(R.id.action_navigation_change_password_to_navigation_account)
                            } else {
                                activity?.runOnUiThread {
                                    Toast.makeText(
                                        context,
                                        response.errorBody()?.string().toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else {
                        val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                        when (error.status.toString()) {
                            "400" ->
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    context,
                                    getString(R.string.old_password_incorrect),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            "401" ->
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    context,
                                    getString(R.string.old_password_incorrect),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else ->
                            activity?.runOnUiThread {
                                Toast.makeText(
                                    context,
                                    response.errorBody()?.string().toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                } catch (exception: SocketTimeoutException) {
                    activity?.runOnUiThread {
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

    /**
     * Funzione di estensione per semplificare l'impostazione di un'azione afterTextChanged sui componenti EditText.
     */
    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}



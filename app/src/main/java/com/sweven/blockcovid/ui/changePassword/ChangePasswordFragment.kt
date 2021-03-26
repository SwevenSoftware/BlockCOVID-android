package com.sweven.blockcovid.ui.changePassword

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
        val oldPassword: TextInputLayout = view.findViewById(R.id.old_password)
        val newPassword: TextInputLayout = view.findViewById(R.id.new_password)
        val repeatPassword: TextInputLayout = view.findViewById(R.id.repeat_password)
        val changePassword: Button = view.findViewById(R.id.change_password_button)
        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)

        val mainActivity = viewLifecycleOwner
        changePasswordViewModel.changePasswordFormState.observe(mainActivity, Observer {
            val changePasswordState = it ?: return@Observer

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

            val retrofit = netClient.getClient()
            val service = retrofit.create(APIChangePassword::class.java)

            val oldPasswordText = editOldPassword.text.toString()
            val newPasswordText = editNewPassword.text.toString()

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
                                    loading.hide()
                                    Toast.makeText(
                                        context,
                                        getString(R.string.password_changed),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                view.findNavController().navigate(R.id.action_navigation_change_password_to_navigation_account)
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
                        val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                        activity?.runOnUiThread {
                            loading.hide()
                            Toast.makeText(
                                    context,
                                    getString(R.string.error).plus(" ").plus(error.error),
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    activity?.runOnUiThread {
                        loading.hide()
                        Toast.makeText(
                                context,
                                e.message,
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



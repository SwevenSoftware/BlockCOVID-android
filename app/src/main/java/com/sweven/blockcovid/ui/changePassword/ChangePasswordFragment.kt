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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sweven.blockcovid.R
import java.io.File

class ChangePasswordFragment : Fragment() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changePasswordViewModel =
            ViewModelProvider(this, ChangePasswordViewModelFactory()).get(ChangePasswordViewModel::class.java)
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

        changePasswordViewModel.changePasswordFormState.observe(
            mainActivity,
            {
                checkPasswordFormState(changePassword, it, oldPassword, newPassword, repeatPassword)
            }
        )

        changePasswordViewModel.changePasswordResult.observe(
            mainActivity,
            {
                checkPasswordResult(it, loading, editOldPassword, editNewPassword, editRepeatPassword)
            }
        )

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
            changePasswordViewModel.changePassword(oldPasswordText, newPasswordText, authorization)
        }
    }
    fun checkPasswordFormState(
        changePassword: Button,
        changePasswordState: ChangePasswordFormState,
        oldPassword: TextInputLayout,
        newPassword: TextInputLayout,
        repeatPassword: TextInputLayout
    ) {
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

    fun checkPasswordResult(
        formResult: ChangePasswordResult,
        loading: CircularProgressIndicator,
        oldPassword: TextInputEditText,
        newPassword: TextInputEditText,
        repeatPassword: TextInputEditText
    ) {
        loading.hide()
        if (formResult.success != null) {
            Toast.makeText(context, getString(R.string.password_changed), Toast.LENGTH_SHORT).show()
            view?.findNavController()?.navigateUp()
        } else if (formResult.error != null) {
            oldPassword.text?.clear()
            newPassword.text?.clear()
            repeatPassword.text?.clear()
            showChangePasswordFailed(formResult.error)
        }
    }

    fun showChangePasswordFailed(errorString: String) {
        Toast.makeText(context, getString(R.string.error).plus(" ").plus(errorString), Toast.LENGTH_SHORT).show()
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

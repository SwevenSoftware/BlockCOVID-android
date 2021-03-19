package com.sweven.blockcovid.ui.changePassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.InputChecks
import com.sweven.blockcovid.MainActivity
import com.sweven.blockcovid.R
import com.sweven.blockcovid.ui.login.LoginFormState
import com.sweven.blockcovid.ui.login.afterTextChanged

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



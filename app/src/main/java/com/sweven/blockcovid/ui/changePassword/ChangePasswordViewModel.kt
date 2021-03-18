package com.sweven.blockcovid.ui.changePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.InputChecks
import com.sweven.blockcovid.R

class ChangePasswordViewModel : ViewModel() {

    private val _changePasswordForm = MutableLiveData<ChangePasswordFormState>()
    val changePasswordFormState: LiveData<ChangePasswordFormState> = _changePasswordForm

    fun inputDataChanged(oldPassword: String, newPassword: String, repeatPassword: String) {
        if (!InputChecks.isPasswordValid(oldPassword)) {
            _changePasswordForm.value = ChangePasswordFormState(oldPasswordError = R.string.invalid_password)
        } else if (!InputChecks.isPasswordValid(newPassword)) {
            _changePasswordForm.value = ChangePasswordFormState(newPasswordError = R.string.invalid_password)
        } else if (!InputChecks.isPasswordValid(repeatPassword)) {
            _changePasswordForm.value = ChangePasswordFormState(repeatPasswordError = R.string.invalid_password)
        } else if (!InputChecks.isPasswordSame(newPassword, repeatPassword)) {
            _changePasswordForm.value = ChangePasswordFormState(repeatPasswordError = R.string.different_password)
        }
        else {
            _changePasswordForm.value = ChangePasswordFormState(isDataValid = true)
        }
    }
}

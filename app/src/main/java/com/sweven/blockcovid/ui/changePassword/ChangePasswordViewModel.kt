package com.sweven.blockcovid.ui.changePassword

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweven.blockcovid.InputChecks
import com.sweven.blockcovid.R
import com.sweven.blockcovid.data.ChangePasswordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChangePasswordViewModel(private val changePasswordRepository: ChangePasswordRepository) : ViewModel() {

    private val _changePasswordForm = MutableLiveData<ChangePasswordFormState>()
    val changePasswordFormState: LiveData<ChangePasswordFormState> = _changePasswordForm

    private val _changePasswordResult = MutableLiveData<ChangePasswordResult>()
    val changePasswordResult: LiveData<ChangePasswordResult> = _changePasswordResult

    fun inputDataChanged(oldPassword: String, newPassword: String, repeatPassword: String) {
        if (!InputChecks.isPasswordValid(oldPassword)) {
            _changePasswordForm.value =
                ChangePasswordFormState(oldPasswordError = R.string.invalid_password)
        } else if (!InputChecks.isPasswordValid(newPassword)) {
            _changePasswordForm.value =
                ChangePasswordFormState(newPasswordError = R.string.invalid_password)
        } else if (!InputChecks.isPasswordValid(repeatPassword)) {
            _changePasswordForm.value =
                ChangePasswordFormState(repeatPasswordError = R.string.invalid_password)
        } else if (!InputChecks.isPasswordSame(newPassword, repeatPassword)) {
            _changePasswordForm.value =
                ChangePasswordFormState(repeatPasswordError = R.string.different_password)
        } else {
            _changePasswordForm.value = ChangePasswordFormState(isDataValid = true)
        }
    }

   fun changePasswordRepo(
        oldPassword: String,
        newPassword: String,
        authorization: String
    ) {
       viewModelScope.launch(Dispatchers.IO){
        changePasswordRepository.changePasswordRepo(oldPassword, newPassword, authorization)}
    }
}


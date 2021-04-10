package com.sweven.blockcovid.ui.changePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.InputChecks
import com.sweven.blockcovid.R
import com.sweven.blockcovid.data.ChangePasswordRepository
import com.sweven.blockcovid.data.Result

class ChangePasswordViewModel(private val changePasswordRepository: ChangePasswordRepository) : ViewModel() {

    private val _changePasswordForm = MutableLiveData<ChangePasswordFormState>()
    val changePasswordFormState: LiveData<ChangePasswordFormState>
        get() = _changePasswordForm

    private val _changePasswordResult = MutableLiveData<ChangePasswordResult>()
    val changePasswordResult: LiveData<ChangePasswordResult>
        get() = _changePasswordResult

    fun changePassword(oldPassword: String, newPassword: String, authorization: String) {
        changePasswordRepository.changePassword(oldPassword, newPassword, authorization)
        changePasswordRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _changePasswordResult.postValue(ChangePasswordResult(success = it.data))
                } else if (it is Result.Error) {
                    _changePasswordResult.postValue(ChangePasswordResult(error = it.exception))
                }
            }
        }
    }

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
}


package com.sweven.blockcovid.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sweven.blockcovid.InputChecks
import com.sweven.blockcovid.R
import com.sweven.blockcovid.data.repositories.LoginRepository
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.LoggedInUser


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState>
        get() = _loginForm
    private val _loginResult = MutableLiveData<LoginResult>()
    val  loginResult: LiveData<LoginResult>
        get() = _loginResult

    fun login(username: String, password: String) {
        loginRepository.login(username, password)
        loginRepository.serverResponse.observeForever { it ->
            it.getContentIfNotHandled()?.let {
                if (it is Result.Success) {
                    _loginResult.postValue(LoginResult(success =
                    LoggedInUser(
                            displayName = it.data.displayName, token = it.data.token,
                            expiryDate = it.data.expiryDate, authority = it.data.authority
                    )
                    ))
                } else if (it is Result.Error) {
                    _loginResult.postValue(LoginResult(error = it.exception))
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!InputChecks.isUsernameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!InputChecks.isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }
}

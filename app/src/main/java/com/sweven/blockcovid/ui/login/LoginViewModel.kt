package com.sweven.blockcovid.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweven.blockcovid.InputChecks
import com.sweven.blockcovid.data.LoginRepository
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = loginRepository.login(username, password)

                 if (result is Result.Success) {
                _loginResult.postValue(LoginResult(success =
                    LoggedInUserView(displayName = result.data.displayName, token = result.data.token, expiryDate = result.data.expiryDate)))
					
                } else if (result is Result.Error) {
                    _loginResult.postValue(LoginResult(error = result.exception))
                }
            } catch (exception: SocketTimeoutException) {
                println("socketexception")
                _loginResult.postValue(LoginResult(error = "Timeout"))
            }
            catch (exception: ConnectException) {
                println("connecttimeout")
                _loginResult.postValue(LoginResult(error = "Connection error"))
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

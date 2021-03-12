package com.sweven.blockcovid.ui.login

import android.provider.Settings.Global.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.viewModelScope
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
    val status = MutableLiveData<Int?>()
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = loginRepository.login(username, password)

                if (result is Result.Success) {
                    _loginResult.postValue(
                        LoginResult(
                            success =
                            LoggedInUserView(
                                displayName = result.data.displayName,
                                token = result.data.token
                            )
                        )
                    )
                } else {
                    _loginResult.postValue(LoginResult(error = R.string.login_failed))
                }
            } catch (exception: SocketTimeoutException) {
                println("sockettimeout")
                status.postValue(0)
            }
            catch (exception: ConnectException) {
                println("connecttimeout")
                status.postValue(1)
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

package com.sweven.blockcovid.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.InputChecks
import com.sweven.blockcovid.data.LoginRepository
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.*
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.LoggedInUser

class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockLoginViewModel: LoginViewModel
    private lateinit var mockLoginRepository: LoginRepository

    @Before
    fun setUp() {
        mockLoginRepository = spy(LoginRepository::class.java)
        mockLoginViewModel = spy(LoginViewModel(mockLoginRepository))
    }

    @Test
    fun login_success() {
        val mockLoginResult: LiveData<LoginResult> = mock()
        val mockServerResponse: LiveData<Event<Result<LoggedInUser>>> = mock()
        val result = Result.Success(LoggedInUser("admin", "token", 123456789, "ADMIN"))

        doNothing().`when`(mockLoginRepository).login("admin", "password")
        doReturn(mockLoginResult).`when`(mockLoginViewModel).loginResult
        doReturn(mockServerResponse).`when`(mockLoginRepository).serverResponse

        mockLoginRepository.triggerEvent(result)
        mockLoginViewModel.login("admin", "password")

//        assertTrue(mockLoginResult.value?.success?.displayName == "admin")
//        assertTrue(mockLoginResult.value?.success?.token == "token")
//        assertTrue(mockLoginResult.value?.success?.expiryDate == 123456789.toLong())
//        assertTrue(mockLoginResult.value?.success?.authority == "ADMIN")
    }

    @Test
    fun login_error() {
        val mockLoginResult: LiveData<LoginResult> = mock()
        val mockServerResponse: LiveData<Event<Result<LoggedInUser>>> = mock()
        val result = Result.Error("error")

        doNothing().`when`(mockLoginRepository).login("admin", "password")
        doReturn(mockLoginResult).`when`(mockLoginViewModel).loginResult
        doReturn(mockServerResponse).`when`(mockLoginRepository).serverResponse

        mockLoginRepository.triggerEvent(result)
        mockLoginViewModel.login("admin", "password")

//        assertTrue(mockLoginResult.value?.error == "error")
    }

    @Test
    fun loginDataChanged_correct() {
        val mockInputChecks: InputChecks = mock()
        val mockLoginFormState: LiveData<LoginFormState> = mock()
        doReturn(mockLoginFormState).`when`(mockLoginViewModel).loginFormState
        doReturn(true).`when`(mockInputChecks).isFieldNotEmpty("username")
        doReturn(true).`when`(mockInputChecks).isPasswordValid("password")

        mockLoginViewModel.loginDataChanged("username", "password")
    }

    @Test
    fun loginDataChanged_userError() {
        val mockInputChecks: InputChecks = mock()
        val mockLoginFormState: LiveData<LoginFormState> = mock()
        doReturn(mockLoginFormState).`when`(mockLoginViewModel).loginFormState
        doReturn(false).`when`(mockInputChecks).isFieldNotEmpty("")

        mockLoginViewModel.loginDataChanged("", "password")
    }

    @Test
    fun loginDataChanged_passwordError() {
        val mockInputChecks: InputChecks = mock()
        val mockLoginFormState: LiveData<LoginFormState> = mock()
        doReturn(mockLoginFormState).`when`(mockLoginViewModel).loginFormState
        doReturn(false).`when`(mockInputChecks).isPasswordValid("pass")

        mockLoginViewModel.loginDataChanged("username", "pass")
    }
}
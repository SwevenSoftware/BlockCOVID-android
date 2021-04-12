package com.sweven.blockcovid.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.LoginRepository
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.*
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.LoggedInUser
import org.junit.Assert.*

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
        val result = Result.Success(LoggedInUser("admin", "token", 123456789, "ADMIN"))

        doNothing().`when`(mockLoginRepository).login("admin", "password")

        mockLoginRepository.triggerEvent(result)
        mockLoginViewModel.login("admin", "password")

        assertTrue(mockLoginViewModel.loginResult.value?.success?.displayName == "admin")
        assertTrue(mockLoginViewModel.loginResult.value?.success?.token == "token")
        assertTrue(mockLoginViewModel.loginResult.value?.success?.expiryDate == 123456789.toLong())
        assertTrue(mockLoginViewModel.loginResult.value?.success?.authority == "ADMIN")
    }

    @Test
    fun login_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockLoginRepository).login("admin", "password")

        mockLoginRepository.triggerEvent(result)
        mockLoginViewModel.login("admin", "password")

        assertTrue(mockLoginViewModel.loginResult.value?.error == "error")
    }

    @Test
    fun loginDataChanged_correct() {
        mockLoginViewModel.loginDataChanged("username", "password")
        assertTrue(mockLoginViewModel.loginFormState.value?.usernameError == null)
        assertTrue(mockLoginViewModel.loginFormState.value?.passwordError == null)
        assertTrue(mockLoginViewModel.loginFormState.value?.isDataValid!!)
    }

    @Test
    fun loginDataChanged_userError() {
        mockLoginViewModel.loginDataChanged("", "password")
        assertTrue(mockLoginViewModel.loginFormState.value?.usernameError != null)
        assertTrue(mockLoginViewModel.loginFormState.value?.passwordError == null)
        assertTrue(!mockLoginViewModel.loginFormState.value?.isDataValid!!)
    }

    @Test
    fun loginDataChanged_passwordError() {
        mockLoginViewModel.loginDataChanged("username", "pass")
        assertTrue(mockLoginViewModel.loginFormState.value?.usernameError == null)
        assertTrue(mockLoginViewModel.loginFormState.value?.passwordError != null)
        assertTrue(!mockLoginViewModel.loginFormState.value?.isDataValid!!)
    }
}
package com.sweven.blockcovid.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.data.repositories.LoginRepository
import com.sweven.blockcovid.services.NetworkClient
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy

class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockLoginViewModel: LoginViewModel
    private lateinit var mockLoginRepository: LoginRepository
    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockLoginRepository = spy(LoginRepository(mockNetworkClient))
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

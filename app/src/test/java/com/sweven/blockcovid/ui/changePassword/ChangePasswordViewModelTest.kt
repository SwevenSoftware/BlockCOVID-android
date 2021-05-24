package com.sweven.blockcovid.ui.changePassword

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.repositories.ChangePasswordRepository
import com.sweven.blockcovid.services.NetworkClient
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ChangePasswordViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockChangePasswordViewModel: ChangePasswordViewModel
    private lateinit var mockChangePasswordRepository: ChangePasswordRepository
    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockChangePasswordRepository = Mockito.spy(ChangePasswordRepository(mockNetworkClient))
        mockChangePasswordViewModel = Mockito.spy(ChangePasswordViewModel(mockChangePasswordRepository))
    }

    @Test
    fun testChangePassword_success() {
        val result = Result.Success("success")

        Mockito.doNothing().`when`(mockChangePasswordRepository).changePassword("password", "password", "password")

        mockChangePasswordRepository.triggerEvent(result)
        mockChangePasswordViewModel.changePassword("password", "password", "password")

        Assert.assertTrue(mockChangePasswordViewModel.changePasswordResult.value?.success == "success")
    }

    @Test
    fun testChangePassword_error() {
        val result = Result.Error("error")

        Mockito.doNothing().`when`(mockChangePasswordRepository).changePassword("wrong_password", "password", "password")

        mockChangePasswordRepository.triggerEvent(result)
        mockChangePasswordViewModel.changePassword("wrong_password", "password", "password")

        Assert.assertTrue(mockChangePasswordViewModel.changePasswordResult.value?.error == "error")
    }

    @Test
    fun testInputDataChanged_correct() {
        mockChangePasswordViewModel.inputDataChanged("password", "password", "password")
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.oldPasswordError == null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.newPasswordError == null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.repeatPasswordError == null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.isDataValid!!)
    }

    @Test
    fun testInputDataChanged_oldPassShort() {
        mockChangePasswordViewModel.inputDataChanged("pass", "password", "password")
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.oldPasswordError != null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.newPasswordError == null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.repeatPasswordError == null)
        Assert.assertTrue(!mockChangePasswordViewModel.changePasswordFormState.value?.isDataValid!!)
    }

    @Test
    fun testInputDataChanged_newPassShort() {
        mockChangePasswordViewModel.inputDataChanged("password", "pass", "password")
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.oldPasswordError == null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.newPasswordError != null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.repeatPasswordError == null)
        Assert.assertTrue(!mockChangePasswordViewModel.changePasswordFormState.value?.isDataValid!!)
    }

    @Test
    fun testInputDataChanged_repeatPassShort() {
        mockChangePasswordViewModel.inputDataChanged("password", "password", "pass")
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.oldPasswordError == null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.newPasswordError == null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.repeatPasswordError != null)
        Assert.assertTrue(!mockChangePasswordViewModel.changePasswordFormState.value?.isDataValid!!)
    }

    @Test
    fun testInputDataChanged_differentPass() {
        mockChangePasswordViewModel.inputDataChanged("password", "password1", "password2")
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.oldPasswordError == null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.newPasswordError == null)
        Assert.assertTrue(mockChangePasswordViewModel.changePasswordFormState.value?.repeatPasswordError != null)
        Assert.assertTrue(!mockChangePasswordViewModel.changePasswordFormState.value?.isDataValid!!)
    }
}

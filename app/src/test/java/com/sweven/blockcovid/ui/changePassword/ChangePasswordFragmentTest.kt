package com.sweven.blockcovid.ui.changePassword

import android.view.View
import android.widget.Button
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sweven.blockcovid.data.repositories.ChangePasswordRepository
import org.jetbrains.annotations.NotNull
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

class ChangePasswordFragmentTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var fragmentTest: ChangePasswordFragment
    private lateinit var mockChangePasswordViewModel: ChangePasswordViewModel

    private lateinit var mockChangePasswordFormState: LiveData<ChangePasswordFormState>
    private lateinit var mockChangePasswordResult: LiveData<ChangePasswordResult>
    private lateinit var mockRepository: ChangePasswordRepository
    private lateinit var mockView: View
    private lateinit var mockNavController: NavController

    @Before
    fun setUp() {
        mockChangePasswordViewModel = mock()
        mockChangePasswordFormState = mock()
        mockChangePasswordResult = mock()
        mockRepository = mock()

        fragmentTest = Mockito.spy(ChangePasswordFragment())
        mockChangePasswordViewModel = Mockito.spy(ChangePasswordViewModel(mockRepository))
        mockView = mock()
        mockNavController = mock()
        Navigation.setViewNavController(mockView, mockNavController)

    }

    @Test
    fun checkChangePasswordFormState_correct() {
        val formState = ChangePasswordFormState(
            oldPasswordError = null,
            newPasswordError = null,
            repeatPasswordError = null,
            isDataValid = true
        )
        @NotNull val changePasswordButton: Button = mock()
        @NotNull val oldPasswordLayout: TextInputLayout = mock()
        @NotNull val newPasswordLayout: TextInputLayout = mock()
        @NotNull val repeatPasswordLayout: TextInputLayout = mock()

        Mockito.doReturn(mockChangePasswordFormState).`when`(mockChangePasswordViewModel).changePasswordFormState

        fragmentTest.checkPasswordFormState(changePasswordButton, formState, oldPasswordLayout, newPasswordLayout, repeatPasswordLayout)
        Assert.assertTrue(oldPasswordLayout.error == null)
        Assert.assertTrue(newPasswordLayout.error == null)
        Assert.assertTrue(repeatPasswordLayout.error == null)
    }

    @Test
    fun checkChangePasswordFormState_oldPassError() {
        val formState = ChangePasswordFormState(
            oldPasswordError = 1,
            newPasswordError = null,
            repeatPasswordError = null,
            isDataValid = false
        )
        @NotNull val changePasswordButton: Button = mock()
        @NotNull val oldPasswordLayout: TextInputLayout = mock()
        @NotNull val newPasswordLayout: TextInputLayout = mock()
        @NotNull val repeatPasswordLayout: TextInputLayout = mock()

        Mockito.doReturn(mockChangePasswordFormState).`when`(mockChangePasswordViewModel).changePasswordFormState
        Mockito.doReturn("error").`when`(fragmentTest).getString(anyInt())
        Mockito.doReturn("pass_error").`when`(oldPasswordLayout).error

        fragmentTest.checkPasswordFormState(changePasswordButton, formState, oldPasswordLayout, newPasswordLayout, repeatPasswordLayout)
        Assert.assertTrue(oldPasswordLayout.error != null)
        Assert.assertTrue(newPasswordLayout.error == null)
        Assert.assertTrue(repeatPasswordLayout.error == null)
    }

    @Test
    fun checkChangePasswordFormState_newPassError() {
        val formState = ChangePasswordFormState(
            oldPasswordError = null,
            newPasswordError = 1,
            repeatPasswordError = null,
            isDataValid = false
        )
        @NotNull val changePasswordButton: Button = mock()
        @NotNull val oldPasswordLayout: TextInputLayout = mock()
        @NotNull val newPasswordLayout: TextInputLayout = mock()
        @NotNull val repeatPasswordLayout: TextInputLayout = mock()

        Mockito.doReturn(mockChangePasswordFormState).`when`(mockChangePasswordViewModel).changePasswordFormState
        Mockito.doReturn("error").`when`(fragmentTest).getString(anyInt())
        Mockito.doReturn("pass_error").`when`(newPasswordLayout).error

        fragmentTest.checkPasswordFormState(changePasswordButton, formState, oldPasswordLayout, newPasswordLayout, repeatPasswordLayout)
        Assert.assertTrue(oldPasswordLayout.error == null)
        Assert.assertTrue(newPasswordLayout.error != null)
        Assert.assertTrue(repeatPasswordLayout.error == null)
    }

    @Test
    fun checkChangePasswordFormState_repeatPassError() {
        val formState = ChangePasswordFormState(
            oldPasswordError = null,
            newPasswordError = null,
            repeatPasswordError = 1,
            isDataValid = false
        )
        @NotNull val changePasswordButton: Button = mock()
        @NotNull val oldPasswordLayout: TextInputLayout = mock()
        @NotNull val newPasswordLayout: TextInputLayout = mock()
        @NotNull val repeatPasswordLayout: TextInputLayout = mock()

        Mockito.doReturn(mockChangePasswordFormState).`when`(mockChangePasswordViewModel).changePasswordFormState
        Mockito.doReturn("error").`when`(fragmentTest).getString(anyInt())
        Mockito.doReturn("pass_error").`when`(repeatPasswordLayout).error

        fragmentTest.checkPasswordFormState(changePasswordButton, formState, oldPasswordLayout, newPasswordLayout, repeatPasswordLayout)
        Assert.assertTrue(oldPasswordLayout.error == null)
        Assert.assertTrue(newPasswordLayout.error == null)
        Assert.assertTrue(repeatPasswordLayout.error != null)
    }

    @Test
    fun checkChangePasswordFormState_allPassError() {
        val formState = ChangePasswordFormState(
            oldPasswordError = 1,
            newPasswordError = 1,
            repeatPasswordError = 1,
            isDataValid = false
        )
        @NotNull val changePasswordButton: Button = mock()
        @NotNull val oldPasswordLayout: TextInputLayout = mock()
        @NotNull val newPasswordLayout: TextInputLayout = mock()
        @NotNull val repeatPasswordLayout: TextInputLayout = mock()

        Mockito.doReturn(mockChangePasswordFormState).`when`(mockChangePasswordViewModel).changePasswordFormState
        Mockito.doReturn("error").`when`(fragmentTest).getString(anyInt())
        Mockito.doReturn("pass_error").`when`(oldPasswordLayout).error
        Mockito.doReturn("pass_error").`when`(newPasswordLayout).error
        Mockito.doReturn("pass_error").`when`(repeatPasswordLayout).error

        fragmentTest.checkPasswordFormState(changePasswordButton, formState, oldPasswordLayout, newPasswordLayout, repeatPasswordLayout)
        Assert.assertTrue(oldPasswordLayout.error != null)
        Assert.assertTrue(newPasswordLayout.error != null)
        Assert.assertTrue(repeatPasswordLayout.error != null)
    }

    @Test
    fun checkChangePasswordResult_success() {
//        val formResult = ChangePasswordResult(
//            success = "success"
//        )
//        @NotNull val loading: CircularProgressIndicator = mock()
//        @NotNull val oldPassword: TextInputEditText = mock()
//        @NotNull val newPassword: TextInputEditText = mock()
//        @NotNull val repeatPassword: TextInputEditText = mock()
//
//        Mockito.doReturn(mockChangePasswordResult).`when`(mockChangePasswordViewModel).changePasswordResult
//
//        Mockito.`when`(fragmentTest.view).thenReturn(mockView)
//        Mockito.`when`(mockView.findNavController()).thenReturn(mockNavController)
//        Mockito.`when`(mockNavController.navigateUp()).thenReturn(true)
//
//        fragmentTest.checkPasswordResult(formResult, loading, oldPassword, newPassword, repeatPassword)
//        Assert.assertTrue(!loading.isShown)
    }

    @Test
    fun checkChangePasswordResult_error() {
        val formResult = ChangePasswordResult(
            error = "error"
        )
        @NotNull val loading: CircularProgressIndicator = mock()
        @NotNull val oldPassword: TextInputEditText = mock()
        oldPassword.setText("test")
        @NotNull val newPassword: TextInputEditText = mock()
        newPassword.setText("test")
        @NotNull val repeatPassword: TextInputEditText = mock()
        repeatPassword.setText("test")

        Mockito.doReturn(mockChangePasswordResult).`when`(mockChangePasswordViewModel).changePasswordResult
        Mockito.doNothing().`when`(fragmentTest).showChangePasswordFailed(anyString())

        fragmentTest.checkPasswordResult(formResult, loading, oldPassword, newPassword, repeatPassword)
        Assert.assertTrue(!loading.isShown)
        Assert.assertTrue(oldPassword.text == null)
        Assert.assertTrue(newPassword.text == null)
        Assert.assertTrue(repeatPassword.text == null)
    }
}
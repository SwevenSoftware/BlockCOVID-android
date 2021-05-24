package com.sweven.blockcovid.ui.login

import android.content.Context
import android.widget.Button
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sweven.blockcovid.R
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.data.repositories.LoginRepository
import org.jetbrains.annotations.NotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import java.io.File

class LoginActivityTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var activityTest: LoginActivity
    private lateinit var mockLoginViewModel: LoginViewModel

    private lateinit var mockLoginFormState: LiveData<LoginFormState>
    private lateinit var mockLoginResult: LiveData<LoginResult>
    private lateinit var mockRepository: LoginRepository

    @Before
    fun setUp() {
        mockLoginViewModel = mock()
        mockLoginFormState = mock()
        mockLoginResult = mock()
        mockRepository = mock()

        activityTest = spy(LoginActivity())
        mockLoginViewModel = spy(LoginViewModel(mockRepository))
    }

    @Test
    fun checkLoginFormState_correct() {
        val formState = LoginFormState(
            usernameError = null,
            passwordError = null,
            isDataValid = true
        )
        @NotNull val loginButton: Button = mock()
        @NotNull val usernameLayout: TextInputLayout = mock()
        @NotNull val passwordLayout: TextInputLayout = mock()

        doReturn(mockLoginFormState).`when`(mockLoginViewModel).loginFormState
        doReturn(usernameLayout).`when`(activityTest).findViewById<TextInputLayout>(R.id.username_layout)
        doReturn(passwordLayout).`when`(activityTest).findViewById<TextInputLayout>(R.id.password_layout)

        activityTest.checkLoginFormState(formState, loginButton)
        assertTrue(usernameLayout.error == null)
        assertTrue(passwordLayout.error == null)
    }

    @Test
    fun checkLoginFormState_userError() {
        val formState = LoginFormState(
            usernameError = 1,
            passwordError = null,
            isDataValid = false
        )
        @NotNull val loginButton: Button = mock()
        @NotNull val usernameLayout: TextInputLayout = mock()
        @NotNull val passwordLayout: TextInputLayout = mock()

        doReturn(mockLoginFormState).`when`(mockLoginViewModel).loginFormState
        doReturn(usernameLayout).`when`(activityTest).findViewById<TextInputLayout>(R.id.username_layout)
        doReturn(passwordLayout).`when`(activityTest).findViewById<TextInputLayout>(R.id.password_layout)
        doReturn("user_error").`when`(activityTest).getString(formState.usernameError!!)
        doReturn("user_error").`when`(usernameLayout).error

        activityTest.checkLoginFormState(formState, loginButton)
        assertTrue(usernameLayout.error != null)
    }

    @Test
    fun checkLoginFormState_passError() {
        val formState = LoginFormState(
            usernameError = null,
            passwordError = 1,
            isDataValid = false
        )
        @NotNull val loginButton: Button = mock()
        @NotNull val usernameLayout: TextInputLayout = mock()
        @NotNull val passwordLayout: TextInputLayout = mock()

        doReturn(mockLoginFormState).`when`(mockLoginViewModel).loginFormState
        doReturn(usernameLayout).`when`(activityTest).findViewById<TextInputLayout>(R.id.username_layout)
        doReturn(passwordLayout).`when`(activityTest).findViewById<TextInputLayout>(R.id.password_layout)
        doReturn("pass_error").`when`(activityTest).getString(formState.passwordError!!)
        doReturn("pass_error").`when`(passwordLayout).error

        activityTest.checkLoginFormState(formState, loginButton)
        assertTrue(passwordLayout.error != null)
    }

    @Test
    fun checkLoginResult_null() {

        @NotNull val formResult: LoginResult = mock()
        @NotNull val loading: CircularProgressIndicator = mock()
        @NotNull val editUsername: TextInputEditText = mock()
        @NotNull val editPassword: TextInputEditText = mock()

        doNothing().`when`(activityTest).showLoginFailed(any())
        doReturn(mockLoginResult).`when`(mockLoginViewModel).loginResult

        activityTest.checkLoginResult(formResult, loading, editUsername, editPassword)
        assertTrue(!loading.isShown)
        assertTrue(editUsername.text == null)
        assertTrue(editPassword.text == null)
    }

    @Test
    fun checkLoginResult_cleaner() {
        val formResult = LoginResult(
            success = LoggedInUser(
                displayName = "username",
                token = "token",
                expiryDate = 12,
                authority = "USER"
            )
        )
        @NotNull val loading: CircularProgressIndicator = mock()
        @NotNull val editUsername: TextInputEditText = mock()
        @NotNull val editPassword: TextInputEditText = mock()

        doNothing().`when`(activityTest).updateUiWithUser(formResult.success!!)
        doNothing().`when`(activityTest).saveToken(formResult.success!!)

        doReturn(formResult.success!!.authority).`when`(activityTest).getCacheAuth()
        doReturn(mockLoginResult).`when`(mockLoginViewModel).loginResult

        activityTest.checkLoginResult(formResult, loading, editUsername, editPassword)
        assertTrue(!loading.isShown)
    }

    @Test
    fun checkLoginResult_user() {
        val formResult = LoginResult(
            success = LoggedInUser(
                displayName = "cleaner",
                token = "token",
                expiryDate = 12,
                authority = "CLEANER"
            )
        )
        @NotNull val loading: CircularProgressIndicator = mock()
        @NotNull val editUsername: TextInputEditText = mock()
        @NotNull val editPassword: TextInputEditText = mock()

        doNothing().`when`(activityTest).updateUiWithUser(formResult.success!!)
        doNothing().`when`(activityTest).saveToken(formResult.success!!)

        doReturn(formResult.success!!.authority).`when`(activityTest).getCacheAuth()
        doReturn(mockLoginResult).`when`(mockLoginViewModel).loginResult

        activityTest.checkLoginResult(formResult, loading, editUsername, editPassword)
        assertTrue(!loading.isShown)
    }

    @Test
    fun checkLoginResult_error() {
        val formResult = LoginResult(
            success = LoggedInUser(
                displayName = "username",
                token = "token",
                expiryDate = 12,
                authority = "USER"
            )
        )
        @NotNull val loading: CircularProgressIndicator = mock()
        @NotNull val editUsername: TextInputEditText = mock()
        @NotNull val editPassword: TextInputEditText = mock()

        doNothing().`when`(activityTest).updateUiWithUser(formResult.success!!)
        doNothing().`when`(activityTest).saveToken(formResult.success!!)

        doReturn(formResult.success!!.authority).`when`(activityTest).getCacheAuth()
        doReturn(mockLoginResult).`when`(mockLoginViewModel).loginResult

        activityTest.checkLoginResult(formResult, loading, editUsername, editPassword)
        assertTrue(!loading.isShown)
    }

    @Test
    fun getAuthToken_correct() {
        val context: Context = mock()
        File.createTempFile("authority", null, context.cacheDir)
        val cacheAuth = File(context.cacheDir, "authority")
        cacheAuth.writeText("test")
        activityTest.getCacheAuth()
        val token = activityTest.getCacheAuth()
        assertTrue(token == "test")
        cacheAuth.delete()
    }

    @Test
    fun saveToken_correct() {
        val context: Context = mock()
        File.createTempFile("token", null, context.cacheDir)
        File.createTempFile("expiryDate", null, context.cacheDir)
        File.createTempFile("username", null, context.cacheDir)
        File.createTempFile("authority", null, context.cacheDir)
        val cacheToken = File(context.cacheDir, "token")
        val cacheExpiry = File(context.cacheDir, "expiryDate")
        val cacheUser = File(context.cacheDir, "username")
        val cacheAuth = File(context.cacheDir, "authority")

        val formResult = LoginResult(
            success =
            LoggedInUser(
                displayName = "username",
                token = "token",
                expiryDate = 12,
                authority = "USER"
            )
        )

        doReturn(context).`when`(activityTest).applicationContext

        activityTest.saveToken(formResult.success!!)
        assertTrue(cacheUser.readText() == "username")
        assertTrue(cacheToken.readText() == "token")
        assertTrue(cacheExpiry.readText() == "12")
        assertTrue(cacheAuth.readText() == "USER")

        cacheToken.delete()
        cacheExpiry.delete()
        cacheUser.delete()
        cacheAuth.delete()
    }
}

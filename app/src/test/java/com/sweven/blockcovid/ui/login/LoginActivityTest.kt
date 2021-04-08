package com.sweven.blockcovid.ui.login

import android.os.Bundle
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito.*


class LoginActivityTest {
    private lateinit var activityTest : LoginActivity

    @Before
    fun setUp() {
        activityTest= spy(LoginActivity())
    }

    @Test
    fun onCreate() {
        val mockLoginViewModel : LoginViewModel= mock(LoginViewModel::class.java)
        doReturn(mockLoginViewModel).`when`(activityTest).createNewLoginModel()

        val mockLoginFormState : LoginFormState= mock(LoginFormState::class.java)
        doReturn(mockLoginFormState).`when`(mockLoginViewModel).getLoginFormState()
        verify(activityTest).checkLoginState(any(),any())

        val mockLoginResult : LoginResult= mock(LoginResult::class.java)
        doReturn(mockLoginResult).`when`(mockLoginViewModel).getLoginResult()
        verify(activityTest).checkLoginResult(any(),any(),any(),any())
    }

    @Test
    fun checkLoginState_null() {

    }
}
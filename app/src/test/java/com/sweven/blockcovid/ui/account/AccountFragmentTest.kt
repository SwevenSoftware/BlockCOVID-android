package com.sweven.blockcovid.ui.account

import com.sweven.blockcovid.ui.login.LoginActivity
import com.sweven.blockcovid.ui.login.LoginResult
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.powermock.api.mockito.PowerMockito

class AccountFragmentTest {
    private lateinit var activityTest : AccountFragment

    @Before
    fun setUp() {
       activityTest = PowerMockito.spy(AccountFragment())
    }

}
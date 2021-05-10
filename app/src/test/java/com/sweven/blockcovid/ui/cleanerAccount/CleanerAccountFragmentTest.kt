package com.sweven.blockcovid.ui.cleanerAccount

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import java.io.File

class CleanerAccountFragmentTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var fragmentTest: CleanerAccountFragment

    @Before
    fun setUp() {
        fragmentTest = Mockito.spy(CleanerAccountFragment())
    }

    @Test
    fun logoutClearToken_test() {
        val context: Context = mock()

        File.createTempFile("token", null, context.cacheDir)
        File.createTempFile("expiryDate", null, context.cacheDir)
        File.createTempFile("username", null, context.cacheDir)
        File.createTempFile("authority", null, context.cacheDir)
        File.createTempFile("reservationId", null, context.cacheDir)
        File.createTempFile("reservationEndTime", null, context.cacheDir)
        val cacheToken = File(context.cacheDir, "token")
        val cacheExpiry = File(context.cacheDir, "expiryDate")
        val cacheUser = File(context.cacheDir, "username")
        val cacheAuth = File(context.cacheDir, "authority")
        val cacheReservationId = File(context.cacheDir, "reservationId")
        val reservationEndTime = File(context.cacheDir, "reservationEndTime")

        fragmentTest.logoutClearToken(cacheToken, cacheExpiry, cacheUser, cacheAuth, cacheReservationId, reservationEndTime)
        assertTrue(!cacheToken.exists())
        assertTrue(!cacheExpiry.exists())
        assertTrue(!cacheUser.exists())
        assertTrue(!cacheAuth.exists())
        assertTrue(!cacheReservationId.exists())
        assertTrue(!reservationEndTime.exists())
    }
}
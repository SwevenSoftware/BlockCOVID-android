package com.sweven.blockcovid.data

import com.sweven.blockcovid.data.model.LoggedInUser
import org.junit.Test

import org.junit.Assert.*

class ResultTest {

    @Test
    fun testToString() {
        val resultSuccess = Result.Success(LoggedInUser("admin", "token", 123456789, "ADMIN"))
        val resultError = Result.Error("error")

        val resultSuccessToString = resultSuccess.toString()
        val resultErrorToString = resultError.toString()

        assertTrue(resultSuccessToString == "Success(data=LoggedInUser(displayName=admin, token=token, expiryDate=123456789, authority=ADMIN))")
        assertTrue(resultErrorToString == "Error(exception=error)")
    }
}
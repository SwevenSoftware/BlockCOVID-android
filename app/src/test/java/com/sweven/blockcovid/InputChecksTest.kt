package com.sweven.blockcovid

import com.sweven.blockcovid.ui.login.InputChecks
import org.junit.Test
import org.junit.Assert.*

class InputChecksTest {

    @Test
    fun isPasswordValid() {
        assertTrue("La password è valida", InputChecks.isPasswordValid("password"))
        assertFalse("La password è invalida", InputChecks.isPasswordValid("pass"))
    }
}
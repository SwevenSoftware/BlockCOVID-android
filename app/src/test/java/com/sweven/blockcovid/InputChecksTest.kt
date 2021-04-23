package com.sweven.blockcovid

import org.junit.Test
import org.junit.Assert.*

class InputChecksTest {

    @Test
    fun isUsernameValid() {
        assertTrue("L'username è valido", InputChecks.isUsernameValid("uS3rname"))
        assertFalse("L'username è invalido", InputChecks.isUsernameValid(""))
        assertFalse("L'username è invalido", InputChecks.isUsernameValid("use"))
        assertFalse("L'username è invalido", InputChecks.isUsernameValid("usernamethatistoolong"))
        assertFalse("L'username è invalido", InputChecks.isUsernameValid("invalidchar!"))
    }

    @Test
    fun isPasswordValid() {
        assertTrue("La password è valida", InputChecks.isPasswordValid("password"))
        assertFalse("La password è invalida", InputChecks.isPasswordValid("pass"))
    }

    @Test
    fun isPasswordSame() {
        assertTrue("Le password corrispondono", InputChecks.isPasswordSame("password", "password"))
        assertFalse("Le password non corrispondono", InputChecks.isPasswordSame("password", "passwords"))
    }
}

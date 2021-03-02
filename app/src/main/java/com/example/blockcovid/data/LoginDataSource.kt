package com.example.blockcovid.data

import com.example.blockcovid.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String, token: String): Result<LoggedInUser> {
        try {
            val user = LoggedInUser(java.util.UUID.randomUUID().toString(), username, token)
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}
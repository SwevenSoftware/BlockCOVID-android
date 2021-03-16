package com.sweven.blockcovid.data

import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.NetworkClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * Classe che richiede l'autenticazione e le informazioni sull'utente dall'origine dati remota e
  * mantiene una cache in memoria dello stato di accesso e delle informazioni sulle credenziali dell'utente.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // cache in memoria dell'oggetto loggedInUser
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // Se le credenziali dell'utente verranno memorizzate nella cache nella memoria locale, si consiglia di crittografarle
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {

        val retrofit = NetworkClient.retrofitClient

        val service = retrofit.create(APIUser::class.java)

        val jsonObject = JSONObject()
        jsonObject.put("username", username)
        jsonObject.put("password", password)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val response = service.loginUser(requestBody)
        return if (response.isSuccessful) {
            val token = response.body()?.string().toString()
            print("Token: ")
            println(token)

            val result = dataSource.login(username, password, token)
            if (result is Result.Success) {
                setLoggedInUser(result.data)
            }
            return result
        } else {
            Result.Error(IOException("Error logging in"))
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // Se le credenziali dell'utente verranno memorizzate nella cache nella memoria locale, si consiglia di crittografarle
    }
}

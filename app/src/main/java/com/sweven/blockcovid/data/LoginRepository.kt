package com.sweven.blockcovid.data

import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.NetworkClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

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
            val items = response.body()
            if (response.errorBody() != null) {
                if(items != null) {
                    var token = ""
                    var expiryDate: Long = 0
                    for(i in 0 until items.count()) {
                        token = items[i].token ?: "N/A"
                        print("Token: ")
                        println(token)
                        val expiryDateISO = items[i].expiryDate ?: "N/A"
                        val expiryDateLDT = LocalDateTime.parse(expiryDateISO)
                        // Scadenza di scadenza del token in millisecondi
                        expiryDate = expiryDateLDT.toEpochSecond(UTC)

                        print("expiryDate: ")
                        println(expiryDate)
                    }
                    val result = dataSource.login(username, password, token, expiryDate)
                    if (result is Result.Success) {
                        setLoggedInUser(result.data)
                    }
                    return result
                } else {
                    return Result.Error(IOException("Qualcosa è andato molto storto se sei qui"))
                }
            } else {
                // 404
                println("404")
                return Result.Error(IOException("404"))
            }
        } else {
            // altri errori (400, 401, etc.)
            return Result.Error(IOException("Error logging in"))
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // Se le credenziali dell'utente verranno memorizzate nella cache nella memoria locale, si consiglia di crittografarle
    }
}

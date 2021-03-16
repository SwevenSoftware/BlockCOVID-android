package com.sweven.blockcovid.data

import com.google.gson.Gson
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.ErrorBody
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
        if (response.isSuccessful) {
            if (response.errorBody() == null) {
                val items = response.body()
                if(items != null) {
                    val id = items.id
                    print("Token: ")
                    println(id)

                    val expiryDateISO = items.expiryDate
                    val expiryDateLDT = LocalDateTime.parse(expiryDateISO)
                    // Scadenza di scadenza del token in millisecondi
                    val expiryDate = expiryDateLDT.toEpochSecond(UTC)
                    print("expiryDate: ")
                    println(expiryDate)

                    val result = dataSource.login(username, password, id, expiryDate)
                    if (result is Result.Success) {
                        setLoggedInUser(result.data)
                    }
                    return result
                } else {
                    return Result.Error("Non dovresti essere qui")
                }
            } else {
                println("Successful but error")
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                return Result.Error(error.status.toString())
            }
        } else {
           println("Not successful")
            val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
            return Result.Error(error.status.toString())
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // Se le credenziali dell'utente verranno memorizzate nella cache nella memoria locale, si consiglia di crittografarle
    }
}

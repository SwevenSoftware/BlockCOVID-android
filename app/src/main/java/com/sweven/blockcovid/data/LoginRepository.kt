package com.sweven.blockcovid.data

import com.google.gson.Gson
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.NetworkClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

/**
 * Classe che richiede l'autenticazione e le informazioni sull'utente dall'origine dati remota e
  * mantiene una cache in memoria dello stato di accesso e delle informazioni sulle credenziali dell'utente.
 */

class LoginRepository {

    // cache in memoria dell'oggetto loggedInUser
    var user: LoggedInUser? = null
        private set

    init {
        // Se le credenziali dell'utente verranno memorizzate nella cache nella memoria locale, si consiglia di crittografarle
        user = null
    }

    private var netClient = NetworkClient()

    fun setNetwork(nc: NetworkClient) {
        netClient = nc
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {

        val retrofit = netClient.getClient()

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
                    val id = items.token.id
                    println(id)

                    val expiryDateISO = items.token.expiryDate
                    val expiryDateLDT = LocalDateTime.parse(expiryDateISO)
                    // Scadenza di scadenza del token in millisecondi
                    val expiryDate = expiryDateLDT.toEpochSecond(UTC)

                    val authority = items.authoritiesList[0]

                    val user = LoggedInUser(username, id, expiryDate, authority)
                    val result = Result.Success(user)
                    setLoggedInUser(result.data)
                    return result
                } else {
                    return Result.Error("Non dovresti essere qui")
                }
            } else {
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                return Result.Error(error.error)
            }
        } else {
            val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
            return Result.Error(error.error)
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // Se le credenziali dell'utente verranno memorizzate nella cache nella memoria locale, si consiglia di crittografarle
    }
}

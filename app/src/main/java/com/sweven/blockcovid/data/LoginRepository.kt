package com.sweven.blockcovid.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.TokenAuthorities
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

/**
 * Classe che richiede l'autenticazione e le informazioni sull'utente dall'origine dati remota.
 */

class LoginRepository {

    private val _serverResponse = MutableLiveData<Event<Result<LoggedInUser>>>()
    val serverResponse: LiveData<Event<Result<LoggedInUser>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<LoggedInUser>) {
        _serverResponse.value = Event(value)
    }

    fun login(username: String, password: String) {

        val retrofit = NetworkClient.buildService(APIUser::class.java)

        val jsonObject = JSONObject()
        jsonObject.put("username", username)
        jsonObject.put("password", password)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        retrofit.loginUser(requestBody).enqueue(object: Callback<TokenAuthorities> {
            override fun onFailure(call: Call<TokenAuthorities>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<TokenAuthorities>, response: Response<TokenAuthorities>) {
                if (response.errorBody() == null) {
                    val items = response.body()

                    val id = items!!.token.id
                    println(id)

                    val expiryDateISO = items.token.expiryDate
                    val expiryDateLDT = LocalDateTime.parse(expiryDateISO)
                    // Scadenza di scadenza del token in millisecondi
                    val expiryDate = expiryDateLDT.toEpochSecond(UTC)

                    val authority = items.authoritiesList[0]

                    val user = LoggedInUser(username, id, expiryDate, authority)
                    val result = Result.Success(user)
                    triggerEvent(result)
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
}

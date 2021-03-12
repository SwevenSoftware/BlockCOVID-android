package com.sweven.blockcovid.data

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sweven.blockcovid.R
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.NetworkClient
import java.io.IOException

/**
 * Classe che richiede l'autenticazione e le informazioni sull'utente dall'origine dati remota e
  * mantiene una cache in memoria dello stato di accesso e delle informazioni sulle credenziali dell'utente.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // cache in memoria dell'oggetto loggedInUser
    var user: LoggedInUser? = null
        private set

    val status = MutableLiveData<Int?>()

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

        val fields: HashMap<String?, String?> = HashMap()
        fields["username"] = (username)
        fields["password"] = (password)

        val response = service.loginUser(fields)
        if (response.isSuccessful) {
            if (response.errorBody() != null) {


                val token = response.body()?.string().toString()
                print("Token: ")
                println(token)

                val result = dataSource.login(username, password, token)
                if (result is Result.Success) {
                    setLoggedInUser(result.data)
                }
                return result
            } else {
                val result = Result.Error(IOException("Error logging in"))
                when (response.errorBody().toString()) {
                    "404" -> status.postValue(2)
                }
                return result
            }

        }
        val result = Result.Error(IOException("Error logging in"))
        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // Se le credenziali dell'utente verranno memorizzate nella cache nella memoria locale, si consiglia di crittografarle
    }
}

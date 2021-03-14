package com.sweven.blockcovid.data

import com.google.gson.Gson
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.ErrorBody
import com.sweven.blockcovid.services.NetworkClient

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
                println("Successful")
                return result
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

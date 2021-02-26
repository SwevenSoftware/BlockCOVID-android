package com.example.blockcovid.data

import com.example.blockcovid.data.model.LoggedInUser
import com.example.blockcovid.services.users.APIUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .build()

        val service = retrofit.create(APIUser::class.java)

        val fields: HashMap<String?, String?> = HashMap()
        fields["username"] = (username)
        fields["password"] = (password)

        val result = dataSource.login(username, password)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.uploadLogin(fields)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    // Convert raw JSON to pretty JSON using GSON library
                    //val gson = GsonBuilder().setPrettyPrinting().create()
                    //val prettyJson = gson.toJson(
                    //    JsonParser.parseString(
                    //        response.body()
                    //            ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    //    )
                    //)
                    //Log.d("Pretty Printed JSON :", prettyJson)
                    val logResponse = response.body()?.toString()
                    println(logResponse)
                    if (result is Result.Success) {
                        setLoggedInUser(result.data)
                    } else {
                        result is Result.Error
                        //Log.e("RETROFIT_ERROR", response.code().toString())

                    }
                }
            }
        }
        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
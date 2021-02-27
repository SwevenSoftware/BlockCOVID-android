package com.example.blockcovid.data

import com.example.blockcovid.data.model.LoggedInUser
import com.example.blockcovid.services.APIUser
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

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

        val BASE_URL = "http://192.168.1.91:8080"
        val TIMEOUT = 10
        val retrofit: Retrofit?
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build()

        val service = retrofit.create(APIUser::class.java)

        val fields: HashMap<String?, String?> = HashMap()
        fields["username"] = (username)
        fields["password"] = (password)

        var token = ""

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.loginUser(fields)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    token = response.body()?.string().toString()
                    print("Token: ")
                    println(token)
                }
            }
        }

        val result = dataSource.login(username, password, token)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
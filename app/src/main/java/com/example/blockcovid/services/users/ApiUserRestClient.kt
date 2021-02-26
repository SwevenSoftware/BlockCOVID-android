package com.example.blockcovid.services.users

import com.example.blockcovid.services.network.NetworkClient
import com.example.blockcovid.services.network.RetrofitEventListener
import com.example.blockcovid.services.users.model.User
import java.util.HashMap


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApiUserRestClient {

    companion object {
        val instance = ApiUserRestClient()
    }

    private var mApiUser: APIUser? = null

    /**
     * Invoke getUserList via [Call] request.
     * @param retrofitEventListener of RetrofitEventListener.
     */

    //     https://reqres.in/api/users?page=1
        suspend fun uploadLogin(retrofitEventListener: RetrofitEventListener) {
        val retrofit = NetworkClient.retrofitClient
        mApiUser = retrofit.create(APIUser::class.java)

        val data = HashMap<String?, String?>()

        val apiUserCall = mApiUser!!.uploadLogin(data)
        /*
        This is the line which actually sends a network request. Calling enqueue() executes a call asynchronously. It has two callback listeners which will invoked on the main thread
        */

        //apiUserCall.enqueue(object : Callback<User?> {

            //override fun onResponse(call: Call<User?>, response: Response<User?>) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                //if (response.body() != null) {
                    //retrofitEventListener.onSuccess(call, response.body())
                //}
            //}
            //override fun onFailure(call: Call<User?>, t: Throwable?) {
                /*
                Error callback
                */
                //retrofitEventListener.onError(call, t)
            //}
        //})

    }

}
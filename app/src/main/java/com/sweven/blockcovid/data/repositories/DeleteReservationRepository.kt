package com.sweven.blockcovid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIDeleteReservation
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.services.gsonReceive.Reservation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteReservationRepository(private val networkClient: NetworkClient) {

    private val _serverResponse = MutableLiveData<Event<Result<String>>>()
    val serverResponse: LiveData<Event<Result<String>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<String>) {
        _serverResponse.value = Event(value)
    }

    fun deleteReservation(idReservation: String, authorization: String) {

        val call = networkClient.buildService(APIDeleteReservation::class.java).deleteReservation(authorization, idReservation)

        call.enqueue(object : Callback<Reservation> {
            override fun onFailure(call: Call<Reservation>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<Reservation>, response: Response<Reservation>) {
                if (response.errorBody() == null) {
                    val responseString = response.body().toString()
                    val result = Result.Success(responseString)
                    triggerEvent(result)
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
}

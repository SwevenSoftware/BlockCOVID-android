package com.sweven.blockcovid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.DeskStatus
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIDeskStatus
import com.sweven.blockcovid.services.gsonReceive.DeskStatusLinks
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeskStatusRepository(private val networkClient: NetworkClient) {

    private val _serverResponse = MutableLiveData<Event<Result<DeskStatus>>>()
    val serverResponse: LiveData<Event<Result<DeskStatus>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<DeskStatus>) {
        _serverResponse.value = Event(value)
    }

    fun deskStatus(authorization: String, timestamp: String, deskId: String) {

        val call = networkClient.buildService(APIDeskStatus::class.java).getDeskStatus(authorization, timestamp, deskId)

        call.enqueue(object : Callback<DeskStatusLinks> {
            override fun onFailure(call: Call<DeskStatusLinks>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<DeskStatusLinks>, response: Response<DeskStatusLinks>) {
                if (response.errorBody() == null) {
                    val available = response.body()?.available
                    val nextChange = response.body()?.nextChange?.dropLast(3)?.replace("T", " - ")
                    triggerEvent(Result.Success(DeskStatus(available = available, nextChange = nextChange)))
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
}
package com.sweven.blockcovid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.ThisDeskInfo
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIDeskInfo
import com.sweven.blockcovid.services.gsonReceive.DeskInfo
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeskInfoRepository(private val networkClient: NetworkClient) {

    private val _serverResponse = MutableLiveData<Event<Result<ThisDeskInfo>>>()
    val serverResponse: LiveData<Event<Result<ThisDeskInfo>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<ThisDeskInfo>) {
        _serverResponse.value = Event(value)
    }

    fun deskInfo(authorization: String, deskId: String) {

        val call = networkClient.buildService(APIDeskInfo::class.java).getDeskInfo(authorization, deskId)

        call.enqueue(object : Callback<DeskInfo> {
            override fun onFailure(call: Call<DeskInfo>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<DeskInfo>, response: Response<DeskInfo>) {
                if (response.errorBody() == null) {
                    val deskStatus = response.body()
                    if (deskStatus != null) {
                        val x = response.body()?.x
                        val y = response.body()?.y
                        val room = response.body()?.roomName
                        val deskClean = response.body()?.status
                        triggerEvent(Result.Success(ThisDeskInfo(x, y, room, deskClean)))
                    } else {
                        triggerEvent(Result.Success(ThisDeskInfo(null, null, null, null)))
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }
}

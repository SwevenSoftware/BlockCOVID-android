package com.sweven.blockcovid.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.apis.APIReserve
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationRepository(private val networkClient: NetworkClient) {

    private val _serverResponse = MutableLiveData<Event<Result<String>>>()
    val serverResponse: LiveData<Event<Result<String>>>
        get() = _serverResponse

    fun triggerEvent(value: Result<String>) {
        _serverResponse.value = Event(value)
    }

    fun reserve(nameRoom: String, x: Int, y: Int, date: String,
                       from: String, to: String, authorization: String) {

        val requestBody = makeJsonObject(x, y, date, from, to)

        val call = networkClient.buildService(APIReserve::class.java).deskReserve(nameRoom, requestBody, authorization)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                triggerEvent(Result.Error(t.message!!))
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.errorBody() == null) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val responseJson =
                            gson.toJson(JsonParser.parseString(response.body()?.string()))
                    print("Response: ")
                    println(responseJson)
                    val result = Result.Success(responseJson)
                    triggerEvent(result)
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    triggerEvent(Result.Error(error.error))
                }
            }
        })
    }

    fun makeJsonObject(x: Int, y: Int, date: String, from: String, to: String): RequestBody {
        val jsonObject = JSONObject()
        jsonObject.put("x", x.toString().toInt())
        jsonObject.put("y", y.toString().toInt())
        jsonObject.put("date", date)
        jsonObject.put("from", from)
        jsonObject.put("to", to)

        val jsonObjectString = jsonObject.toString()
        return jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
    }
}
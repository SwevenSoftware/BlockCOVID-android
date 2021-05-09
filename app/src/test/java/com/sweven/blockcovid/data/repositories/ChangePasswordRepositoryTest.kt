package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIChangePassword
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class ChangePasswordRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockChangePasswordRepository: ChangePasswordRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIChangePassword
    private lateinit var mockRequestBody: RequestBody
    private lateinit var mockCall: Call<ResponseBody>

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockChangePasswordRepository = Mockito.spy(ChangePasswordRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun changePassword_correct() {
        Mockito.doReturn(mockRequestBody).`when`(mockChangePasswordRepository).makeJsonObject(Mockito.anyString(), Mockito.anyString())
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIChangePassword::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).changePassword("token", mockRequestBody)

        val response = ResponseBody.create("application/json".toMediaTypeOrNull(), "success")

        Mockito.doAnswer { invocation ->
            val callback: Callback<ResponseBody> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockChangePasswordRepository.changePassword("password", "password", "token")
        assertTrue(mockChangePasswordRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun changePassword_error() {
        Mockito.doReturn(mockRequestBody).`when`(mockChangePasswordRepository).makeJsonObject(Mockito.anyString(), Mockito.anyString())
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIChangePassword::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).changePassword("token", mockRequestBody)

        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<ResponseBody> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockChangePasswordRepository.changePassword("wrong_password", "password", "token")
        assertTrue(mockChangePasswordRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun changePassword_exception() {
        Mockito.doReturn(mockRequestBody).`when`(mockChangePasswordRepository).makeJsonObject(Mockito.anyString(), Mockito.anyString())
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIChangePassword::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).changePassword("token", mockRequestBody)

        val response = ResponseBody.create("application/json".toMediaTypeOrNull(), "timeout")

        Mockito.doAnswer { invocation ->
            val callback: Callback<ResponseBody> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockChangePasswordRepository.changePassword("password", "password", "token")
        assertTrue(mockChangePasswordRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }
}
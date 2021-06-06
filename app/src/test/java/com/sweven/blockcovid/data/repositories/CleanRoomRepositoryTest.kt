package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIClean
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class CleanRoomRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockCleanRoomRepository: CleanRoomRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIClean
    private lateinit var mockRequestBody: RequestBody
    private lateinit var mockCall: Call<ResponseBody>

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockCleanRoomRepository = Mockito.spy(CleanRoomRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun changePassword_correct() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIClean::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).cleanRoom(Mockito.anyString(), Mockito.anyString())

        val response = "success".toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<ResponseBody> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockCleanRoomRepository.cleanRoom("authorization", "stanza1")
        assertTrue(mockCleanRoomRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun changePassword_error() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIClean::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).cleanRoom(Mockito.anyString(), Mockito.anyString())

        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<ResponseBody> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockCleanRoomRepository.cleanRoom("wrong_password", "password")
        assertTrue(mockCleanRoomRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun changePassword_exception() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIClean::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).cleanRoom(Mockito.anyString(), Mockito.anyString())

        Mockito.doAnswer { invocation ->
            val callback: Callback<ResponseBody> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockCleanRoomRepository.cleanRoom("password", "password")
        assertTrue(mockCleanRoomRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }
}

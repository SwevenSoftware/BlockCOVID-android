package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIDeskInfo
import com.sweven.blockcovid.services.gsonReceive.DeskInfo
import com.sweven.blockcovid.services.gsonReceive.Links
import com.sweven.blockcovid.services.gsonReceive.Self
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
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

class DeskInfoRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockDeskInfoRepository: DeskInfoRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIDeskInfo
    private lateinit var mockRequestBody: RequestBody
    private lateinit var mockCall: Call<DeskInfo>

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockDeskInfoRepository = Mockito.spy(DeskInfoRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun deskInfo_correct() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskInfo::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskInfo("token", "stanza1")

        val response = DeskInfo("roomName", "deskID", 32, 45, "status", List(1) { Links(Self("href")) })

        Mockito.doAnswer { invocation ->
            val callback: Callback<DeskInfo> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskInfoRepository.deskInfo("token", "stanza1")
        assertTrue(mockDeskInfoRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun deskInfo_some_null() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskInfo::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskInfo("token", "stanza1")

        val response = null

        Mockito.doAnswer { invocation ->
            val callback: Callback<DeskInfo> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskInfoRepository.deskInfo("token", "stanza1")
        assertTrue(mockDeskInfoRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun deskInfo_error() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskInfo::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskInfo("token", "stanza1")

        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<DeskInfo> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskInfoRepository.deskInfo("token", "stanza1")
        assertTrue(mockDeskInfoRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun deskInfo_exception() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskInfo::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskInfo("token", "stanza1")

        Mockito.doAnswer { invocation ->
            val callback: Callback<DeskInfo> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskInfoRepository.deskInfo("token", "stanza1")
        assertTrue(mockDeskInfoRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }
}

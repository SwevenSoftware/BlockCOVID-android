package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIStartReservation
import com.sweven.blockcovid.services.gsonReceive.NewReservation
import com.sweven.blockcovid.services.gsonReceive.Reservation
import com.sweven.blockcovid.services.gsonReceive.ReservationLinks
import com.sweven.blockcovid.services.gsonReceive.SelfReservations
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

class StartReservationRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockStartReservationRepository: StartReservationRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIStartReservation
    private lateinit var mockRequestBody: RequestBody
    private lateinit var mockCall: Call<Reservation>

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockStartReservationRepository = Mockito.spy(StartReservationRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun changePassword_correct() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIStartReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).startReservation("authorization", "stanza1")

        val response = Reservation(
            "id", "deskID", "room", "username", "start",
            "end", "usageStart", "usageEnd", true, true,
            ReservationLinks(
                NewReservation("link1"), SelfReservations("link2", true),
                SelfReservations("link2", true), SelfReservations("link3", true)
            )
        )

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservation> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockStartReservationRepository.startReservation("stanza1", "authorization")
        assertTrue(mockStartReservationRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun changePassword_error() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIStartReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).startReservation("authorization", "stanza1")

        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservation> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockStartReservationRepository.startReservation("stanza1", "authorization")
        assertTrue(mockStartReservationRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun changePassword_exception() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIStartReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).startReservation("authorization", "stanza1")

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservation> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockStartReservationRepository.startReservation("stanza1", "authorization")
        assertTrue(mockStartReservationRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }
}

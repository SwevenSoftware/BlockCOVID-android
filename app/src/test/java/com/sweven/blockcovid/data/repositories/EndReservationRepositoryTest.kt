package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIEndReservation
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

class EndReservationRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockEndReservationRepository: EndReservationRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIEndReservation
    private lateinit var mockRequestBody: RequestBody
    private lateinit var mockCall: Call<Reservation>

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockEndReservationRepository = Mockito.spy(EndReservationRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun endReservation_correct() {
        Mockito.doReturn(mockRequestBody).`when`(mockEndReservationRepository).makeJsonObject(Mockito.anyBoolean())
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIEndReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).endReservation("password", "password", mockRequestBody)

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

        mockEndReservationRepository.endReservation("password", "password", true)
        assertTrue(mockEndReservationRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun endReservation_error() {
        Mockito.doReturn(mockRequestBody).`when`(mockEndReservationRepository).makeJsonObject(Mockito.anyBoolean())
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIEndReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).endReservation("password", "wrong_password", mockRequestBody)

        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservation> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockEndReservationRepository.endReservation("wrong_password", "password", true)
        assertTrue(mockEndReservationRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun endReservation_exception() {
        Mockito.doReturn(mockRequestBody).`when`(mockEndReservationRepository).makeJsonObject(Mockito.anyBoolean())
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIEndReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).endReservation("password", "password", mockRequestBody)

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservation> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())
        mockEndReservationRepository.endReservation("password", "password", true)
        assertTrue(mockEndReservationRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }
}

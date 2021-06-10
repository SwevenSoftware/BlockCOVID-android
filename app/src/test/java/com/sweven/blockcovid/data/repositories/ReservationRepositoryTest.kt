package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIReserve
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

class ReservationRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockReservationRepository: ReservationRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIReserve
    private lateinit var mockRequestBody: RequestBody
    private lateinit var mockCall: Call<Reservation>

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockReservationRepository = Mockito.spy(ReservationRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun changePassword_correct() {
        Mockito.doReturn(mockRequestBody).`when`(mockReservationRepository).makeJsonObject(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIReserve::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).deskReserve("authorization", mockRequestBody)

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

        mockReservationRepository.reserve("deskid", "09:00", "10:00", "authorization")
        assertTrue(mockReservationRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun changePassword_error() {
        Mockito.doReturn(mockRequestBody).`when`(mockReservationRepository).makeJsonObject(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIReserve::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).deskReserve("authorization", mockRequestBody)

        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservation> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockReservationRepository.reserve("deskid", "09:00", "10:00", "authorization")
        assertTrue(mockReservationRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun changePassword_exception() {
        Mockito.doReturn(mockRequestBody).`when`(mockReservationRepository).makeJsonObject(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIReserve::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).deskReserve("authorization", mockRequestBody)

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservation> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockReservationRepository.reserve("deskid", "09:00", "10:00", "authorization")
        assertTrue(mockReservationRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }
}

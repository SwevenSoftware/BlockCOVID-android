package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIReservations
import com.sweven.blockcovid.services.gsonReceive.Embedded
import com.sweven.blockcovid.services.gsonReceive.NewReservation
import com.sweven.blockcovid.services.gsonReceive.Reservation
import com.sweven.blockcovid.services.gsonReceive.ReservationLinks
import com.sweven.blockcovid.services.gsonReceive.Reservations
import com.sweven.blockcovid.services.gsonReceive.ReservationsLinks
import com.sweven.blockcovid.services.gsonReceive.SelfReservations
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.TimeZone
import java.util.concurrent.TimeoutException

class UserReservationRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockUserReservationsRepository: UserReservationsRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIReservations
    private lateinit var mockRequestBody: Reservations
    private lateinit var mockCall: Call<Reservations>
    private lateinit var mockDateTime: ZonedDateTime

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockUserReservationsRepository = Mockito.spy(UserReservationsRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
        mockDateTime = mock()
    }

    @Test
    fun cleanerRooms_correct() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIReservations::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getReservations("authorization", "timestamp")
        val localDateTime = LocalDateTime.parse("1996-12-04T09:09")
        val zoneDateTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC)
        Mockito.doReturn(zoneDateTime).`when`(mockUserReservationsRepository).UTCToLocalDateTime("1996-12-04T09:09")

        val response = Reservations(
            Embedded(
                List(1) {
                    Reservation(
                        "id", "deskid", "room", "username", "1996-12-04T09:09",
                        "1996-12-04T09:09", "1996-12-04T09:09", "1996-12-04T09:09", true, true,
                        ReservationLinks(
                            NewReservation("link1"), SelfReservations("link2", true),
                            SelfReservations("link2", true), SelfReservations("link3", true)
                        )
                    )
                }
            ),
            ReservationsLinks(SelfReservations("string", true))
        )

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservations> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockUserReservationsRepository.userReservations("authorization", "timestamp")
        assertTrue(mockUserReservationsRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRooms_null() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIReservations::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getReservations("authorization", "timestamp")
        val localDateTime = LocalDateTime.parse("1996-12-04T09:09")
        val zoneDateTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC)
        Mockito.doReturn(zoneDateTime).`when`(mockUserReservationsRepository).UTCToLocalDateTime("1996-12-04T09:09")

        val response = null

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservations> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockUserReservationsRepository.userReservations("authorization", "timestamp")
        assertTrue(mockUserReservationsRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRoom_error() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIReservations::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getReservations("authorization", "timestamp")
        Mockito.doReturn(mockDateTime).`when`(mockUserReservationsRepository).UTCToLocalDateTime("09:00")
        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservations> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockUserReservationsRepository.userReservations("authorization", "timestamp")
        assertTrue(mockUserReservationsRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun cleanerRoom_exception() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIReservations::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getReservations("authorization", "timestamp")
        Mockito.doReturn(mockDateTime).`when`(mockUserReservationsRepository).UTCToLocalDateTime("09:00")

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservations> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockUserReservationsRepository.userReservations("authorization", "timestamp")
        assertTrue(mockUserReservationsRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }

    @Test
    fun utcToLocalDateTime_test() {
        val localDateTime = LocalDateTime.parse("2021-06-23T10:00")
        val result = ZonedDateTime.of(localDateTime, ZoneOffset.UTC).withZoneSameInstant(TimeZone.getDefault().toZoneId())
        assertTrue(mockUserReservationsRepository.UTCToLocalDateTime("2021-06-23T10:00") == result)
    }
}

package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIDeskReservation
import com.sweven.blockcovid.services.gsonReceive.Embedded
import com.sweven.blockcovid.services.gsonReceive.NewReservation
import com.sweven.blockcovid.services.gsonReceive.Reservation
import com.sweven.blockcovid.services.gsonReceive.ReservationLinks
import com.sweven.blockcovid.services.gsonReceive.Reservations
import com.sweven.blockcovid.services.gsonReceive.ReservationsLinks
import com.sweven.blockcovid.services.gsonReceive.Rooms
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
import java.util.concurrent.TimeoutException

class DeskReservationRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockDeskReservationRepository: DeskReservationRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIDeskReservation
    private lateinit var mockRequestBody: Rooms
    private lateinit var mockCall: Call<Reservations>
    private lateinit var mockDateTime: LocalDateTime

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockDeskReservationRepository = Mockito.spy(DeskReservationRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
        mockDateTime = mock()
    }

    @Test
    fun cleanerRooms_correct() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskReservation("authorization", "deskid", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskReservationRepository).UTCToLocalDateTime("1996-12-04T09:09")

        val response = Reservations(
            Embedded(
                List(2) {
                    Reservation(
                        "id", "deskid", "room", "username", "1996-12-04T07:09",
                        "1996-12-04T10:10", "1996-12-04T09:09", "1996-12-04T10:10", true, true,
                        ReservationLinks(
                            NewReservation("link1"), SelfReservations("link2", true),
                            SelfReservations("link2", true), SelfReservations("link3", true)
                        )
                    )
                    Reservation(
                        "id", "deskid", "room", "username", "1996-12-04T09:09",
                        "1996-12-04T10:10", "1996-12-04T09:09", "1996-12-04T10:10", true, true,
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

        mockDeskReservationRepository.deskReservation("authorization", "deskid", "from")
        assertTrue(mockDeskReservationRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRooms_some_null() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskReservation("authorization", "deskid", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskReservationRepository).UTCToLocalDateTime("1996-12-04T09:09")

        val response = Reservations(
            Embedded(
                List(2) {
                    Reservation(
                        "id", "deskid", "room", "username", "1996-12-04T07:09",
                        "1996-12-04T10:10", null, null, true, true,
                        ReservationLinks(
                            NewReservation("link1"), SelfReservations("link2", true),
                            SelfReservations("link2", true), SelfReservations("link3", true)
                        )
                    )
                    Reservation(
                        "id", "deskid", "room", "username", "1996-12-04T05:09",
                        "1996-12-04T10:10", null, null, true, true,
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

        mockDeskReservationRepository.deskReservation("authorization", "deskid", "from")
        assertTrue(mockDeskReservationRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRooms_null() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskReservation("authorization", "deskid", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskReservationRepository).UTCToLocalDateTime("1996-12-04T09:09")

        val response = null

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservations> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskReservationRepository.deskReservation("authorization", "deskid", "from")
        assertTrue(mockDeskReservationRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRoom_error() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskReservation("authorization", "deskid", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskReservationRepository).UTCToLocalDateTime("09:00")
        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservations> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskReservationRepository.deskReservation("authorization", "deskid", "from")
        assertTrue(mockDeskReservationRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun cleanerRoom_exception() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskReservation::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskReservation("authorization", "deskid", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskReservationRepository).UTCToLocalDateTime("09:00")

        Mockito.doAnswer { invocation ->
            val callback: Callback<Reservations> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskReservationRepository.deskReservation("authorization", "deskid", "from")
        assertTrue(mockDeskReservationRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }

    @Test
    fun utcToLocalDateTime_test() {
        val result = LocalDateTime.parse("2021-06-23T12:00")
        assertTrue(mockDeskReservationRepository.UTCToLocalDateTime("2021-06-23T10:00") == result)
    }
}

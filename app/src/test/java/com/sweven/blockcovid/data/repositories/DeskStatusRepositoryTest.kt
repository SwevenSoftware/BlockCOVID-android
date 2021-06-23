package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIDeskStatus
import com.sweven.blockcovid.services.gsonReceive.DeskStatusLinks
import com.sweven.blockcovid.services.gsonReceive.Link
import com.sweven.blockcovid.services.gsonReceive.Rooms
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.TimeZone
import java.util.concurrent.TimeoutException

class DeskStatusRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockDeskStatusRepository: DeskStatusRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIDeskStatus
    private lateinit var mockRequestBody: Rooms
    private lateinit var mockCall: Call<DeskStatusLinks>
    private lateinit var mockDateTime: LocalDateTime

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockDeskStatusRepository = Mockito.spy(DeskStatusRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
        mockDateTime = mock()
    }

    @Test
    fun cleanerRooms_correct() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskStatus::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskStatus("authorization", "timestamp", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskStatusRepository).UTCToLocalDateTime("1996-12-04T09:09")

        val response = DeskStatusLinks(
            true, "1996-12-04T09:09",
            Link(
                "rel", "href", "hreflang",
                "media", "title", "type", "deprecation", "profile", "name"
            )
        )

        Mockito.doAnswer { invocation ->
            val callback: Callback<DeskStatusLinks> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskStatusRepository.deskStatus("authorization", "timestamp", "from")
        assertTrue(mockDeskStatusRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRooms_some_null() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskStatus::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskStatus("authorization", "timestamp", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskStatusRepository).UTCToLocalDateTime("1996-12-04T09:09")

        val response = DeskStatusLinks(
            true, null,
            Link(
                "rel", "href", "hreflang",
                "media", "title", "type", "deprecation", "profile", "name"
            )
        )

        Mockito.doAnswer { invocation ->
            val callback: Callback<DeskStatusLinks> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskStatusRepository.deskStatus("authorization", "timestamp", "from")
        assertTrue(mockDeskStatusRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRooms_null() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskStatus::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskStatus("authorization", "timestamp", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskStatusRepository).UTCToLocalDateTime("1996-12-04T09:09")

        val response = null

        Mockito.doAnswer { invocation ->
            val callback: Callback<DeskStatusLinks> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskStatusRepository.deskStatus("authorization", "timestamp", "from")
        assertTrue(mockDeskStatusRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRoom_error() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskStatus::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskStatus("authorization", "timestamp", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskStatusRepository).UTCToLocalDateTime("09:00")
        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<DeskStatusLinks> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskStatusRepository.deskStatus("authorization", "timestamp", "from")
        assertTrue(mockDeskStatusRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun cleanerRoom_exception() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDeskStatus::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDeskStatus("authorization", "timestamp", "from")
        Mockito.doReturn(mockDateTime).`when`(mockDeskStatusRepository).UTCToLocalDateTime("09:00")

        Mockito.doAnswer { invocation ->
            val callback: Callback<DeskStatusLinks> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockDeskStatusRepository.deskStatus("authorization", "timestamp", "from")
        assertTrue(mockDeskStatusRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }

    @Test
    fun utcToLocalDateTime_test() {
        val utcTime = LocalTime.now(ZoneOffset.UTC)
        val utcDate = LocalDate.now(ZoneOffset.UTC)
        val utcDateTime = LocalDateTime.of(utcDate, utcTime)

        val zonedUtcTimeDate = ZonedDateTime.of(utcDate, utcTime, ZoneOffset.UTC)
        val result = zonedUtcTimeDate.withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime()

        assertTrue(mockDeskStatusRepository.UTCToLocalDateTime(utcDateTime.toString()) == result)
    }
}

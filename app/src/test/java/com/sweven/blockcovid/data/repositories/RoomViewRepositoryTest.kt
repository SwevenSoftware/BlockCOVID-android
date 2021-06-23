package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIDesks
import com.sweven.blockcovid.services.gsonReceive.Desk
import com.sweven.blockcovid.services.gsonReceive.Link
import com.sweven.blockcovid.services.gsonReceive.Room
import com.sweven.blockcovid.services.gsonReceive.RoomWithDesks
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
import java.time.LocalTime
import java.util.concurrent.TimeoutException

class RoomViewRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockRoomViewRepository: RoomViewRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIDesks
    private lateinit var mockRequestBody: Rooms
    private lateinit var mockCall: Call<RoomWithDesks>
    private lateinit var mockTime: LocalTime

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockRoomViewRepository = Mockito.spy(RoomViewRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
        mockTime = mock()
    }

    @Test
    fun cleanerRooms_correct() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDesks::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDesks("authorization", "roomName", "17:00", "18:00")
        Mockito.doReturn("09:00").`when`(mockRoomViewRepository).UTCToLocalTime("17:00")

        val response = RoomWithDesks(
            Room(
                "Stanza", false, "09:00", "11:00", List(1) { "MONDAY" },
                10, 10, "CLEAN"
            ),
            List(1) { Desk("123ABC", 1, 1, true) },
            List(1) {
                Link(
                    "rel", "href", "hreflang",
                    "media", "title", "type", "deprecation", "profile", "name"
                )
            }
        )

        Mockito.doAnswer { invocation ->
            val callback: Callback<RoomWithDesks> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockRoomViewRepository.showRoom("17:00", "18:00", "authorization", "roomName")
        assertTrue(mockRoomViewRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRooms_null() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDesks::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDesks("authorization", "roomName", "17:00", "18:00")
        Mockito.doReturn("09:00").`when`(mockRoomViewRepository).UTCToLocalTime("17:00")

        val response = null

        Mockito.doAnswer { invocation ->
            val callback: Callback<RoomWithDesks> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockRoomViewRepository.showRoom("17:00", "18:00", "authorization", "roomName")
        assertTrue(mockRoomViewRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRoom_error() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDesks::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDesks("authorization", "roomName", "17:00", "18:00")

        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<RoomWithDesks> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockRoomViewRepository.showRoom("17:00", "18:00", "authorization", "roomName")
        assertTrue(mockRoomViewRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun cleanerRoom_exception() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIDesks::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getDesks("authorization", "roomName", "17:00", "18:00")
        Mockito.doAnswer { invocation ->
            val callback: Callback<RoomWithDesks> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockRoomViewRepository.showRoom("17:00", "18:00", "authorization", "roomName")
        assertTrue(mockRoomViewRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }

    @Test
    fun utcToLocalDateTime_test() {
        val result = "12:00"
        assertTrue(mockRoomViewRepository.UTCToLocalTime("10:00") == result)
    }
}

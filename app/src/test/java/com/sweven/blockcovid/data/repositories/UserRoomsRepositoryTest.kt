package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIRooms
import com.sweven.blockcovid.services.gsonReceive.Desk
import com.sweven.blockcovid.services.gsonReceive.EmbeddedRoom
import com.sweven.blockcovid.services.gsonReceive.Links
import com.sweven.blockcovid.services.gsonReceive.Room
import com.sweven.blockcovid.services.gsonReceive.RoomWithDesksList
import com.sweven.blockcovid.services.gsonReceive.RoomWithDesksListLinks
import com.sweven.blockcovid.services.gsonReceive.Rooms
import com.sweven.blockcovid.services.gsonReceive.Self
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
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeoutException

class UserRoomsRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockUserRoomRepository: UserRoomsRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIRooms
    private lateinit var mockRequestBody: Rooms
    private lateinit var mockCall: Call<Rooms>

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockUserRoomRepository = Mockito.spy(UserRoomsRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun cleanerRooms_null() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIRooms::class.java)
        val nowLocalDateTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES).toString()
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getRooms("clean", nowLocalDateTime, nowLocalDateTime)
        Mockito.doReturn("09:00").`when`(mockUserRoomRepository).utcToLocalTime("09:00")
        Mockito.doReturn(true).`when`(mockUserRoomRepository).isOpen("clean", "ct", Array(1) { "array" })

        val response = Rooms(
            EmbeddedRoom(
                List(1) {
                    RoomWithDesksList(
                        Room(
                            "Stanza", false, "02:00:00", "22:00:00", listOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"),
                            10, 10, "CLEAN"
                        ),
                        List(1) { Desk("123ABC", 1, 1, true) },
                        RoomWithDesksListLinks(
                            Self("link1"), Self("link2"),
                            Self("link3")
                        )
                    )
                }
            ),
            Links(Self("link4"))
        )

        Mockito.doAnswer { invocation ->
            val callback: Callback<Rooms> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockUserRoomRepository.userRooms("clean")
        assertTrue(mockUserRoomRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRooms_correct() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIRooms::class.java)
        val nowLocalDateTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES).toString()
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getRooms("clean", nowLocalDateTime, nowLocalDateTime)
        Mockito.doReturn("09:00").`when`(mockUserRoomRepository).utcToLocalTime("09:00")
        Mockito.doReturn(true).`when`(mockUserRoomRepository).isOpen("clean", "ct", Array(1) { "array" })

        val response = null

        Mockito.doAnswer { invocation ->
            val callback: Callback<Rooms> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockUserRoomRepository.userRooms("clean")
        assertTrue(mockUserRoomRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRoom_error() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIRooms::class.java)
        val nowLocalDateTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES).toString()
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getRooms("clean", nowLocalDateTime, nowLocalDateTime)
        Mockito.doReturn("09:00").`when`(mockUserRoomRepository).utcToLocalTime("09:00")
        Mockito.doReturn(true).`when`(mockUserRoomRepository).isOpen("clean", "ct", Array(1) { "array" })

        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<Rooms> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockUserRoomRepository.userRooms("clean")
        assertTrue(mockUserRoomRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun cleanerRoom_exception() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIRooms::class.java)
        val nowLocalDateTime = LocalDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES).toString()
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getRooms("clean", nowLocalDateTime, nowLocalDateTime)
        Mockito.doReturn("09:00").`when`(mockUserRoomRepository).utcToLocalTime("09:00")
        Mockito.doReturn(true).`when`(mockUserRoomRepository).isOpen("clean", "ct", Array(1) { "array" })

        Mockito.doAnswer { invocation ->
            val callback: Callback<Rooms> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockUserRoomRepository.userRooms("clean")
        assertTrue(mockUserRoomRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }

    @Test
    fun isOpen_check() {
        assertTrue(!mockUserRoomRepository.isOpen("10:00", "12:00", arrayOf()))
    }

    @Test
    fun utcToLocalDateTime_test() {
        val result = "12:00"
        assertTrue(mockUserRoomRepository.utcToLocalTime("10:00") == result)
    }
}

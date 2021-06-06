package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIRooms
import com.sweven.blockcovid.services.gsonReceive.*
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
import java.util.concurrent.TimeoutException

class CleanerRoomsRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockCleanerRoomRepository: CleanerRoomsRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIRooms
    private lateinit var mockRequestBody: Rooms
    private lateinit var mockCall: Call<Rooms>

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockCleanerRoomRepository = Mockito.spy(CleanerRoomsRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun cleanerRooms_correct() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIRooms::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getRooms(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())
        Mockito.doReturn("09:00").`when`(mockCleanerRoomRepository).utcToLocalTime(Mockito.anyString())
        Mockito.doReturn(true).`when`(mockCleanerRoomRepository).isOpen(Mockito.anyString(),Mockito.anyString(),Array(Mockito.anyInt()){Mockito.anyString()})

        val response = Rooms(EmbeddedRoom(List(1){ RoomWithDesksList(Room("Stanza", false, "09:00", "11:00", List(1){"MONDAY"},
            10, 10, "CLEAN"), List(1){Desk("123ABC", 1, 1, true)}, RoomWithDesksListLinks(Self("link1"),Self("link2"),
            Self("link3")))}), Links(Self("link4")))

        Mockito.doAnswer { invocation ->
            val callback: Callback<Rooms> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockCleanerRoomRepository.cleanerRooms("clean")
        assertTrue(mockCleanerRoomRepository.serverResponse.value?.peekContent() is Result.Success)
    }

    @Test
    fun cleanerRoom_error() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIRooms::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getRooms(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())


        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        Mockito.doAnswer { invocation ->
            val callback: Callback<Rooms> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockCleanerRoomRepository.cleanerRooms(" ")
        assertTrue(mockCleanerRoomRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Internal Server Error"))
    }

    @Test
    fun cleanerRoom_exception() {
        Mockito.doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIRooms::class.java)
        Mockito.doReturn(mockCall).`when`(mockRetrofit).getRooms(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())


        Mockito.doAnswer { invocation ->
            val callback: Callback<Rooms> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())

        mockCleanerRoomRepository.cleanerRooms("")
        assertTrue(mockCleanerRoomRepository.serverResponse.value?.peekContent() == Result.Error(exception = "Timeout"))
    }
}

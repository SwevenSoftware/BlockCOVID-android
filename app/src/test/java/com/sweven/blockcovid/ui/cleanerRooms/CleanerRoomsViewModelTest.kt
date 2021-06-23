package com.sweven.blockcovid.ui.cleanerRooms

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.CleanerRoomsList
import com.sweven.blockcovid.data.repositories.CleanRoomRepository
import com.sweven.blockcovid.data.repositories.CleanerRoomsRepository
import com.sweven.blockcovid.services.NetworkClient
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy

class CleanerRoomsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockCleanerRoomsViewModel: CleanerRoomsViewModel
    private lateinit var mockCleanerRoomsRepository: CleanerRoomsRepository
    private lateinit var mockCleanRoomRepository: CleanRoomRepository
    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockCleanerRoomsRepository = spy(CleanerRoomsRepository(mockNetworkClient))
        mockCleanRoomRepository = spy(CleanRoomRepository(mockNetworkClient))
        mockCleanerRoomsViewModel = spy(CleanerRoomsViewModel(mockCleanerRoomsRepository, mockCleanRoomRepository))
    }

    @Test
    fun showRooms_success() {
        val result = Result.Success(CleanerRoomsList(Array(1) { "roomName" }, Array(1) { true }, Array(1) { true }))

        doNothing().`when`(mockCleanerRoomsRepository).cleanerRooms("authorization")

        mockCleanerRoomsRepository.triggerEvent(result)
        mockCleanerRoomsViewModel.showRooms("authorization")

        assertTrue(mockCleanerRoomsViewModel.cleanerRoomsResult.value?.success?.roomName!![0] == "roomName")
        assertTrue(mockCleanerRoomsViewModel.cleanerRoomsResult.value?.success?.roomIsCleaned!![0])
        assertTrue(mockCleanerRoomsViewModel.cleanerRoomsResult.value?.success?.roomIsOpen!![0])
    }

    @Test
    fun showRooms_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockCleanerRoomsRepository).cleanerRooms("admin")

        mockCleanerRoomsRepository.triggerEvent(result)
        mockCleanerRoomsViewModel.showRooms("admin")

        assertTrue(mockCleanerRoomsViewModel.cleanerRoomsResult.value?.error == "error")
    }

    @Test
    fun cleanRoom_success() {
        val result = Result.Success("data")

        doNothing().`when`(mockCleanRoomRepository).cleanRoom("authorization", "roomName")

        mockCleanRoomRepository.triggerEvent(result)
        mockCleanerRoomsViewModel.cleanRoom("authorization", "roomName")

        assertTrue(mockCleanerRoomsViewModel.cleanRoomResult.value?.success == "data")
    }

    @Test
    fun cleanRoom_error() {
        val result = Result.Error("exception")

        doNothing().`when`(mockCleanRoomRepository).cleanRoom("authorization", "roomName")

        mockCleanRoomRepository.triggerEvent(result)
        mockCleanerRoomsViewModel.cleanRoom("authorization", "roomName")

        assertTrue(mockCleanerRoomsViewModel.cleanRoomResult.value?.error == "exception")
    }
}

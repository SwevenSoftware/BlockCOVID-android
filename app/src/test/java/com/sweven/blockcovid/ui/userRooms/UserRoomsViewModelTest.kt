package com.sweven.blockcovid.ui.userRooms

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.UserRoomsList
import com.sweven.blockcovid.data.repositories.UserRoomsRepository
import com.sweven.blockcovid.services.NetworkClient
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class UserRoomsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockUserRoomsViewModel: UserRoomsViewModel
    private lateinit var mockUserRoomsRepository: UserRoomsRepository
    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockUserRoomsRepository = Mockito.spy(UserRoomsRepository(mockNetworkClient))
        mockUserRoomsViewModel = Mockito.spy(UserRoomsViewModel(mockUserRoomsRepository))
    }

    @Test
    fun testShowRooms_success() {
        val result = Result.Success(UserRoomsList(Array(1) { "ROOMNAME" }, Array(1) { "MONDAY" }, Array(1) { "ROOMCLOSED" }, Array(1) { Array(1) { "MONDAY" } }, Array(1) { true }))

        Mockito.doNothing().`when`(mockUserRoomsRepository).userRooms("authorization")

        mockUserRoomsRepository.triggerEvent(result)
        mockUserRoomsViewModel.showRooms("authorization")

        Assert.assertTrue(mockUserRoomsViewModel.userRoomsResult.value?.success == UserRoomsList(Array(1) { "ROOMNAME" }, Array(1) { "MONDAY" }, Array(1) { "ROOMCLOSED" }, Array(1) { Array(1) { "MONDAY" } }, Array(1) { true }))
    }

    @Test
    fun testShowRooms_error() {
        val result = Result.Error("error")

        Mockito.doNothing().`when`(mockUserRoomsRepository).userRooms("authorization")

        mockUserRoomsRepository.triggerEvent(result)
        mockUserRoomsViewModel.showRooms("authorization")

        Assert.assertTrue(mockUserRoomsViewModel.userRoomsResult.value?.error == "error")
    }
}

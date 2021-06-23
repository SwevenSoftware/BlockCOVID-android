package com.sweven.blockcovid.ui.roomView

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.RoomDesks
import com.sweven.blockcovid.data.repositories.RoomViewRepository
import com.sweven.blockcovid.services.NetworkClient
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import java.time.LocalDateTime

class RoomViewViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockRoomViewViewModel: RoomViewViewModel

    private lateinit var mockRoomViewRepository: RoomViewRepository

    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()

        mockRoomViewRepository = spy(RoomViewRepository(mockNetworkClient))

        mockRoomViewViewModel = spy(
            RoomViewViewModel(
                mockRoomViewRepository
            )
        )
    }

    @Test
    fun showRoom_success() {
        val result = Result.Success(
            RoomDesks(
                "10:00", "10:00",
                Array(1) { "MONDAY" }, Array(1) { "ID" }, Array(1) { 1 }, Array(1) { 1 }, Array(1) { true }
            )
        )

        doNothing().`when`(mockRoomViewRepository).showRoom("10:00", "10:00", "authorization", "roomName")

        mockRoomViewRepository.triggerEvent(result)
        mockRoomViewViewModel.showRoom("10:00", "10:00", "authorization", "roomName")

        assertTrue(
            mockRoomViewViewModel.roomViewResult.value?.success == RoomDesks(
                "10:00", "10:00",
                Array(1) { "MONDAY" }, Array(1) { "ID" }, Array(1) { 1 }, Array(1) { 1 }, Array(1) { true }
            )
        )
    }

    @Test
    fun showRoom_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockRoomViewRepository).showRoom("10:00", "10:00", "authorization", "roomName")

        mockRoomViewRepository.triggerEvent(result)
        mockRoomViewViewModel.showRoom("10:00", "10:00", "authorization", "roomName")
        assertTrue(mockRoomViewViewModel.roomViewResult.value?.error == "error")
    }

    @Test
    fun inputDataChanged_correct() {
        val localDateTime = LocalDateTime.parse("2021-12-04T10:00")
        mockRoomViewViewModel.inputDataChanged(
            localDateTime, "10:00", "10:00", "2021-12-04", "10:00", "10:00",
            Array(1) { "MONDAY" }
        )
        assertTrue(mockRoomViewViewModel.roomViewForm.value?.isDataValid == false)
        assertTrue(mockRoomViewViewModel.roomViewForm.value?.arrivalTimeError == 2131755176)
        assertTrue(mockRoomViewViewModel.roomViewForm.value?.exitTimeError == 2131755176)
        assertTrue(mockRoomViewViewModel.roomViewForm.value?.selectedDateError == 2131755177)
    }

    @Test
    fun isOpenInterval_test() {
        assertTrue(
            !mockRoomViewViewModel.isOpenInterval("10:00", "10:00", "2021-12-04", "10:00", "10:00", Array(1) { "MONDAY" })
        )
    }
}

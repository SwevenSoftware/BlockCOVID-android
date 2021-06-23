package com.sweven.blockcovid.ui.editReservation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.RoomDesks
import com.sweven.blockcovid.data.repositories.DeleteReservationRepository
import com.sweven.blockcovid.data.repositories.EditReservationRepository
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

class EditReservationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockEditReservationViewModel: EditReservationViewModel

    private lateinit var mockEditReservationRepository: EditReservationRepository
    private lateinit var mockDeleteReservationRepository: DeleteReservationRepository
    private lateinit var mockRoomViewRepository: RoomViewRepository

    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()

        mockEditReservationRepository = spy(EditReservationRepository(mockNetworkClient))
        mockDeleteReservationRepository = spy(DeleteReservationRepository(mockNetworkClient))
        mockRoomViewRepository = spy(RoomViewRepository(mockNetworkClient))

        mockEditReservationViewModel = spy(
            EditReservationViewModel(
                mockEditReservationRepository,
                mockDeleteReservationRepository,
                mockRoomViewRepository
            )
        )
    }

    @Test
    fun editReservation_success() {
        val result = Result.Success("data")

        doNothing().`when`(mockEditReservationRepository)
            .editReservation("uno", "1", "start", "end", "authorization")

        mockEditReservationRepository.triggerEvent(result)
        mockEditReservationViewModel.editReservation("uno", "1", "start", "end", "authorization")

        assertTrue(mockEditReservationViewModel.editReservationResult.value?.success == "data")
    }

    @Test
    fun editReservation_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockEditReservationRepository)
            .editReservation("uno", "1", "start", "end", "authorization")

        mockEditReservationRepository.triggerEvent(result)
        mockEditReservationViewModel.editReservation("uno", "1", "start", "end", "authorization")

        assertTrue(mockEditReservationViewModel.editReservationResult.value?.error == "error")
    }
    @Test
    fun deleteReservation_success() {
        val result = Result.Success("data")

        doNothing().`when`(mockDeleteReservationRepository)
            .deleteReservation("uno", "1")

        mockDeleteReservationRepository.triggerEvent(result)
        mockEditReservationViewModel.deleteReservation("uno", "1")

        assertTrue(mockEditReservationViewModel.deleteReservationResult.value?.success == "data")
    }

    @Test
    fun deleteReservation_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockDeleteReservationRepository).deleteReservation("id", "authorization")

        mockDeleteReservationRepository.triggerEvent(result)
        mockEditReservationViewModel.deleteReservation("id", "authorization")
        assertTrue(mockEditReservationViewModel.deleteReservationResult.value?.error == "error")
    }

    @Test
    fun showRoom_success() {
        val result = Result.Success(
            RoomDesks(
                "10:00", "10:00",
                Array(1) { "MONDAY" }, Array(1) { "ID" }, Array(1) { 1 }, Array(1) { 1 }, Array(1) { true }
            )
        )

        doNothing().`when`(mockRoomViewRepository)
            .showRoom("uno", "1", "authorization", "roomName")

        mockRoomViewRepository.triggerEvent(result)
        mockEditReservationViewModel.showRoom("uno", "1", "authorization", "roomName")

        assertTrue(mockEditReservationViewModel.roomViewResult.value?.success?.openingTime == "10:00")
        assertTrue(mockEditReservationViewModel.roomViewResult.value?.success?.closingTime == "10:00")
        assertTrue(mockEditReservationViewModel.roomViewResult.value?.success?.openingDays!![0] == "MONDAY")
        assertTrue(mockEditReservationViewModel.roomViewResult.value?.success?.idArray!![0] == "ID")
        assertTrue(mockEditReservationViewModel.roomViewResult.value?.success?.xArray!![0] == 1)
        assertTrue(mockEditReservationViewModel.roomViewResult.value?.success?.yArray!![0] == 1)
        assertTrue(mockEditReservationViewModel.roomViewResult.value?.success?.availableArray!![0])
    }

    @Test
    fun showRoom_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockRoomViewRepository)
            .showRoom("10:00", "10:00", "start", "end")

        mockRoomViewRepository.triggerEvent(result)
        mockEditReservationViewModel.showRoom("10:00", "10:00", "start", "end")

        assertTrue(mockEditReservationViewModel.roomViewResult.value?.error == "error")
    }

    @Test
    fun inputDataChanged_correct() {
        val localDateTime = LocalDateTime.parse("2021-12-04T10:00")
        mockEditReservationViewModel.inputDataChanged(
            localDateTime, "10:00", "10:00", "2021-12-04", "10:00", "10:00",
            Array(1) { "MONDAY" }
        )
        assertTrue(mockEditReservationViewModel.editReservationForm.value?.isDataValid == false)
        assertTrue(mockEditReservationViewModel.editReservationForm.value?.arrivalTimeError == 2131755176)
        assertTrue(mockEditReservationViewModel.editReservationForm.value?.exitTimeError == 2131755176)
        assertTrue(mockEditReservationViewModel.editReservationForm.value?.selectedDateError == 2131755177)
    }
}

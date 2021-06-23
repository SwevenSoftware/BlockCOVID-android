package com.sweven.blockcovid.ui.customReservation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.RoomDesks
import com.sweven.blockcovid.data.repositories.ReservationRepository
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

class CustomReservationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockCustomReservationViewModel: CustomReservationViewModel

    private lateinit var mockReservationRepository: ReservationRepository
    private lateinit var mockRoomViewRepository: RoomViewRepository

    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()

        mockReservationRepository = spy(ReservationRepository(mockNetworkClient))
        mockRoomViewRepository = spy(RoomViewRepository(mockNetworkClient))

        mockCustomReservationViewModel = spy(
            CustomReservationViewModel(
                mockReservationRepository,
                mockRoomViewRepository
            )
        )
    }

    @Test
    fun customReservation_success() {
        val result = Result.Success("data")

        doNothing().`when`(mockReservationRepository).reserve("deskId", "10:00", "10:00", "authorization")

        mockReservationRepository.triggerEvent(result)
        mockCustomReservationViewModel.customReservation("deskId", "10:00", "10:00", "authorization")

        assertTrue(mockCustomReservationViewModel.reservationResult.value?.success == "data")
    }

    @Test
    fun customReservation_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockReservationRepository).reserve("deskId", "10:00", "10:00", "authorization")

        mockReservationRepository.triggerEvent(result)
        mockCustomReservationViewModel.customReservation("deskId", "10:00", "10:00", "authorization")

        assertTrue(mockCustomReservationViewModel.reservationResult.value?.error == "error")
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
        mockCustomReservationViewModel.showRoom("10:00", "10:00", "authorization", "roomName")

        assertTrue(
            mockCustomReservationViewModel.roomViewResult.value?.success == RoomDesks(
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
        mockCustomReservationViewModel.showRoom("10:00", "10:00", "authorization", "roomName")
        assertTrue(mockCustomReservationViewModel.roomViewResult.value?.error == "error")
    }

    @Test
    fun inputDataChanged_correct() {
        val localDateTime = LocalDateTime.parse("2021-12-04T10:00")
        mockCustomReservationViewModel.inputDataChanged(
            localDateTime, "10:00", "10:00", "2021-12-04", "10:00", "10:00",
            Array(1) { "MONDAY" }
        )
        assertTrue(mockCustomReservationViewModel.customReservationForm.value?.isDataValid == false)
        assertTrue(mockCustomReservationViewModel.customReservationForm.value?.arrivalTimeError == 2131755176)
        assertTrue(mockCustomReservationViewModel.customReservationForm.value?.exitTimeError == 2131755176)
        assertTrue(mockCustomReservationViewModel.customReservationForm.value?.selectedDateError == 2131755177)
    }

    @Test
    fun inputDataChanged_check_1() {
        val localDateTime = LocalDateTime.parse("2021-01-01T10:00")
        mockCustomReservationViewModel.inputDataChanged(
            localDateTime, "10:00", "09:00", "2021-01-01", "10:00", "10:00",
            Array(1) { "MONDAY" }
        )
        assertTrue(mockCustomReservationViewModel.customReservationForm.value?.isDataValid == false)
    }

    @Test
    fun inputDataChanged_check_2() {
        val localDateTime = LocalDateTime.parse("2021-02-01T10:00")
        mockCustomReservationViewModel.inputDataChanged(
            localDateTime, "10:00", "11:00", "2021-01-01", "10:00", "10:00",
            Array(1) { "MONDAY" }
        )
        assertTrue(mockCustomReservationViewModel.customReservationForm.value?.isDataValid == false)
    }

    @Test
    fun inputDataChanged_check_3() {
        val localDateTime = LocalDateTime.parse("2021-01-01T11:00")
        mockCustomReservationViewModel.inputDataChanged(
            localDateTime, "10:00", "11:00", "2021-01-01", "10:00", "10:00",
            Array(1) { "MONDAY" }
        )
        assertTrue(mockCustomReservationViewModel.customReservationForm.value?.isDataValid == false)
    }

    @Test
    fun isOpenInterval_test0() {
        assertTrue(
            !mockCustomReservationViewModel.isOpenInterval("10:00", "10:00", "2021-12-04", "10:00", "10:00", Array(1) { "MONDAY" })
        )
    }

    @Test
    fun isOpenInterval_test1() {
        assertTrue(
            !mockCustomReservationViewModel.isOpenInterval(
                "10:00", "10:00", "2021-12-04", "09:00", "10:00",
                arrayOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")
            )
        )
    }

    @Test
    fun isOpenInterval_test2() {
        assertTrue(
            mockCustomReservationViewModel.isOpenInterval(
                "10:00", "10:00", "2021-12-04", "09:00", "11:00",
                arrayOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")
            )
        )
    }
}

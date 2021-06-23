package com.sweven.blockcovid.ui.reservation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.repositories.ReservationRepository
import com.sweven.blockcovid.services.NetworkClient
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ReservationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockReservationViewModel: ReservationViewModel
    private lateinit var mockReservationRepository: ReservationRepository
    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockReservationRepository = Mockito.spy(ReservationRepository(mockNetworkClient))
        mockReservationViewModel = Mockito.spy(ReservationViewModel(mockReservationRepository))
    }

    @Test
    fun tesReserve_success() {
        val result = Result.Success("data")

        Mockito.doNothing().`when`(mockReservationRepository).reserve("", "", "", "")

        mockReservationRepository.triggerEvent(result)
        mockReservationViewModel.reserve("", "", "", "")

        Assert.assertTrue(mockReservationViewModel.reservationResult.value?.success == "data")
    }

    @Test
    fun testReserve_error() {
        val result = Result.Error("error")

        Mockito.doNothing().`when`(mockReservationRepository).reserve("", "", "", "")

        mockReservationRepository.triggerEvent(result)
        mockReservationViewModel.reserve("", "", "", "")

        Assert.assertTrue(mockReservationViewModel.reservationResult.value?.error == "error")
    }
}

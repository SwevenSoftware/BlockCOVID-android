package com.sweven.blockcovid.ui.userReservartions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.UserReservationsList
import com.sweven.blockcovid.data.repositories.UserReservationsRepository
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.ui.userReservations.UserReservationsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class UserReservationsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

    private lateinit var mockUserReservationsViewModel: UserReservationsViewModel
    private lateinit var mockUserReservationsRepository: UserReservationsRepository
    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockUserReservationsRepository = Mockito.spy(UserReservationsRepository(mockNetworkClient))
        mockUserReservationsViewModel = Mockito.spy(UserReservationsViewModel(mockUserReservationsRepository))
    }

    @Test
    fun testShowRooms_success() {
        val result = Result.Success(UserReservationsList(Array(1) { "ID" }, Array(1) { "ID" }, Array(1) { "ROOMNAME" }, Array(1) { "10:00" }, Array(1) { "10:00" }, Array(1) { "MONDAY" }))

        Mockito.doNothing().`when`(mockUserReservationsRepository).userReservations("authorization", "from")

        mockUserReservationsRepository.triggerEvent(result)
        mockUserReservationsViewModel.showReservations("authorization", "from")

        Assert.assertTrue(
            mockUserReservationsViewModel.userReservationsResult.value?.success == UserReservationsList(
                Array(1) { "ID" }, Array(1) { "ID" }, Array(1) { "ROOMNAME" },
                Array(1) { "10:00" }, Array(1) { "10:00" }, Array(1) { "MONDAY" }
            )
        )
    }

    @Test
    fun testShowRooms_error() {
        val result = Result.Error("error")

        Mockito.doNothing().`when`(mockUserReservationsRepository).userReservations("authorization", "from")

        mockUserReservationsRepository.triggerEvent(result)
        mockUserReservationsViewModel.showReservations("authorization", "from")

        Assert.assertTrue(mockUserReservationsViewModel.userReservationsResult.value?.error == "error")
    }
}

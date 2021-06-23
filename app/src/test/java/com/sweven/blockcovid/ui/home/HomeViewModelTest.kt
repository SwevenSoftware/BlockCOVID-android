package com.sweven.blockcovid.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.DeskReservationData
import com.sweven.blockcovid.data.model.DeskStatus
import com.sweven.blockcovid.data.model.ThisDeskInfo
import com.sweven.blockcovid.data.repositories.DeleteReservationRepository
import com.sweven.blockcovid.data.repositories.DeskInfoRepository
import com.sweven.blockcovid.data.repositories.DeskReservationRepository
import com.sweven.blockcovid.data.repositories.DeskStatusRepository
import com.sweven.blockcovid.data.repositories.EndReservationRepository
import com.sweven.blockcovid.data.repositories.StartReservationRepository
import com.sweven.blockcovid.services.NetworkClient
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy

class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockHomeViewModel: HomeViewModel

    private lateinit var mockDeskStatusRepository: DeskStatusRepository
    private lateinit var mockDeskInfoRepository: DeskInfoRepository
    private lateinit var mockDeskReservationRepository: DeskReservationRepository
    private lateinit var mockStartRepository: StartReservationRepository
    private lateinit var mockEndRepository: EndReservationRepository
    private lateinit var mockDeleteRepository: DeleteReservationRepository
    private lateinit var mockNetworkClient: NetworkClient

    @Before
    fun setUp() {
        mockNetworkClient = mock()

        mockDeskStatusRepository = spy(DeskStatusRepository(mockNetworkClient))
        mockDeskInfoRepository = spy(DeskInfoRepository(mockNetworkClient))
        mockDeskReservationRepository = spy(DeskReservationRepository(mockNetworkClient))
        mockStartRepository = spy(StartReservationRepository(mockNetworkClient))
        mockEndRepository = spy(EndReservationRepository(mockNetworkClient))
        mockDeleteRepository = spy(DeleteReservationRepository(mockNetworkClient))

        mockHomeViewModel = spy(
            HomeViewModel(
                mockDeskStatusRepository,
                mockDeskInfoRepository,
                mockDeskReservationRepository,
                mockStartRepository,
                mockEndRepository,
                mockDeleteRepository
            )
        )
    }

    @Test
    fun deskStatus_success() {
        val result = Result.Success(DeskStatus(true, "nextChange"))

        doNothing().`when`(mockDeskStatusRepository)
            .deskStatus("uno", "1", "start")

        mockDeskStatusRepository.triggerEvent(result)
        mockHomeViewModel.deskStatus("uno", "1", "start")

        assertTrue(mockHomeViewModel.deskStatusResult.value?.success == DeskStatus(true, "nextChange"))
    }

    @Test
    fun deskStatus_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockDeskStatusRepository)
            .deskStatus("uno", "1", "start")

        mockDeskStatusRepository.triggerEvent(result)
        mockHomeViewModel.deskStatus("uno", "1", "start")

        assertTrue(mockHomeViewModel.deskStatusResult.value?.error == "error")
    }
    @Test
    fun deskInfo_success() {
        val result = Result.Success(ThisDeskInfo(1, 1, "room", "clean"))

        doNothing().`when`(mockDeskInfoRepository).deskInfo("authorization", "deskId")

        mockDeskInfoRepository.triggerEvent(result)
        mockHomeViewModel.deskInfo("authorization", "deskId")

        assertTrue(mockHomeViewModel.deskInfoResult.value?.success == ThisDeskInfo(1, 1, "room", "clean"))
    }

    @Test
    fun deskInfo_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockDeskInfoRepository).deskInfo("authorization", "deskId")

        mockDeskInfoRepository.triggerEvent(result)
        mockHomeViewModel.deskInfo("authorization", "deskId")
        assertTrue(mockHomeViewModel.deskInfoResult.value?.error == "error")
    }

    @Test
    fun deskReservation_success() {
        val result = Result.Success(DeskReservationData("id", "room", "10:00", "10:00", "10:00", "10:00", true))

        doNothing().`when`(mockDeskReservationRepository).deskReservation("authorization", "deskId", "timestamp")

        mockDeskReservationRepository.triggerEvent(result)
        mockHomeViewModel.deskReservation("authorization", "deskId", "timestamp")

        assertTrue(mockHomeViewModel.deskReservationResult.value?.success == DeskReservationData("id", "room", "10:00", "10:00", "10:00", "10:00", true))
    }

    @Test
    fun deskReservation_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockDeskReservationRepository).deskReservation("authorization", "deskId", "timestamp")

        mockDeskReservationRepository.triggerEvent(result)
        mockHomeViewModel.deskReservation("authorization", "deskId", "timestamp")
        assertTrue(mockHomeViewModel.deskReservationResult.value?.error == "error")
    }

    @Test
    fun startReservation_success() {
        val result = Result.Success("data")

        doNothing().`when`(mockStartRepository).startReservation("authorization", "deskId")
        mockStartRepository.triggerEvent(result)
        mockHomeViewModel.startReservation("authorization", "deskId")

        assertTrue(mockHomeViewModel.startReservationResult.value?.success == "data")
    }

    @Test
    fun startReservation_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockStartRepository).startReservation("authorization", "deskId")

        mockStartRepository.triggerEvent(result)
        mockHomeViewModel.startReservation("authorization", "deskId")
        assertTrue(mockHomeViewModel.startReservationResult.value?.error == "error")
    }
    @Test
    fun endReservation_success() {
        val result = Result.Success("data")

        doNothing().`when`(mockEndRepository).endReservation("authorization", "deskId", true)
        mockEndRepository.triggerEvent(result)
        mockHomeViewModel.endReservation("authorization", "deskId", true)

        assertTrue(mockHomeViewModel.endReservationResult.value?.success == "data")
    }

    @Test
    fun endReservation_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockEndRepository).endReservation("authorization", "deskId", true)

        mockEndRepository.triggerEvent(result)
        mockHomeViewModel.endReservation("authorization", "deskId", true)
        assertTrue(mockHomeViewModel.endReservationResult.value?.error == "error")
    }

    @Test
    fun deleteReservation_success() {
        val result = Result.Success("data")

        doNothing().`when`(mockDeleteRepository).deleteReservation("authorization", "deskId")
        mockDeleteRepository.triggerEvent(result)
        mockHomeViewModel.deleteReservation("authorization", "deskId")

        assertTrue(mockHomeViewModel.deleteReservationResult.value?.success == "data")
    }

    @Test
    fun deleteReservation_error() {
        val result = Result.Error("error")

        doNothing().`when`(mockDeleteRepository).deleteReservation("authorization", "deskId")

        mockDeleteRepository.triggerEvent(result)
        mockHomeViewModel.deleteReservation("authorization", "deskId")
        assertTrue(mockHomeViewModel.deleteReservationResult.value?.error == "error")
    }
}

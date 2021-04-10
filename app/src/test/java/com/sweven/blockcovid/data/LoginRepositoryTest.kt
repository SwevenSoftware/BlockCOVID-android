package com.sweven.blockcovid.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.NetworkClient
import org.json.JSONObject
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.*

class LoginRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockLoginRepository: LoginRepository
    private lateinit var mockServerResponse: LiveData<Event<Result<LoggedInUser>>>
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIUser

    @Before
    fun setUp() {
        mockLoginRepository = spy(LoginRepository::class.java)
        mockServerResponse = mock()
        mockNetworkClient = mock()
        mockRetrofit = mock()
    }

    @Test
    fun login_correct() {
        val mockJsonObject: JSONObject = mock()
        doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIUser::class.java)
        doReturn("{\"username\":\"admin\",\"password\":\"password\"}").`when`(mockJsonObject).toString()
        //mockLoginRepository.login("admin", "password")
    }
}
package com.sweven.blockcovid.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.sweven.blockcovid.Event
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.APIUser
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.Token
import com.sweven.blockcovid.services.gsonReceive.TokenAuthorities
import okhttp3.RequestBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockLoginRepository: LoginRepository
    private lateinit var mockServerResponse: LiveData<Event<Result<LoggedInUser>>>
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIUser
    private lateinit var mockRequestBody: RequestBody
    private lateinit var mockCall: Call<TokenAuthorities>

    @Before
    fun setUp() {
        mockLoginRepository = spy(LoginRepository::class.java)
        mockServerResponse = mock()
        mockNetworkClient = mock()
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun login_correct() {
        doReturn(mockRequestBody).`when`(mockLoginRepository).makeJsonObject(
            anyString(),
            anyString()
        )
        doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIUser::class.java)
        doReturn(mockCall).`when`(mockRetrofit).loginUser(mockRequestBody)

        //val jsonRequest = mockLoginRepository.makeJsonObject("admin", "password")

        val response =
                TokenAuthorities(
                    authoritiesList = listOf("USER"),
                    token = Token("token", "expiryDate", "admin")
                )

        doAnswer { invocation ->
            val callback: Callback<TokenAuthorities> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(any())

        mockLoginRepository.login("admin", "password")
        //println(mockLoginRepository.serverResponse.value?.peekContent())
    }
}
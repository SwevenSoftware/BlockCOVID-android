package com.sweven.blockcovid.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sweven.blockcovid.data.Result
import com.sweven.blockcovid.data.model.LoggedInUser
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.apis.APIUser
import com.sweven.blockcovid.services.gsonReceive.Token
import com.sweven.blockcovid.services.gsonReceive.TokenAuthorities
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeoutException

class LoginRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private inline fun <reified T> mock(): T = mock(T::class.java)

    private lateinit var mockLoginRepository: LoginRepository
    private lateinit var mockNetworkClient: NetworkClient
    private lateinit var mockRetrofit: APIUser
    private lateinit var mockRequestBody: RequestBody
    private lateinit var mockCall: Call<TokenAuthorities>

    @Before
    fun setUp() {
        mockNetworkClient = mock()
        mockLoginRepository = spy(LoginRepository(mockNetworkClient))
        mockRetrofit = mock()
        mockRequestBody = mock()
        mockCall = mock()
    }

    @Test
    fun login_correct() {
        doReturn(mockRequestBody).`when`(mockLoginRepository).makeJsonObject(anyString(), anyString())
        doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIUser::class.java)
        doReturn(mockCall).`when`(mockRetrofit).loginUser(mockRequestBody)

        val response = TokenAuthorities(
            authoritiesList = listOf("ADMIN"),
            token = Token("bdee5ded-bb59-408f-92d6-d2be4da516cb", "2021-04-15T15:26:59.057965508", "admin")
        )

        doAnswer { invocation ->
            val callback: Callback<TokenAuthorities> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.success(response))
            null
        }.`when`(mockCall).enqueue(any())

        mockLoginRepository.login("admin", "password")
        assertTrue(
            mockLoginRepository.serverResponse.value?.peekContent() ==
                Result.Success(
                    LoggedInUser(
                        "admin", "bdee5ded-bb59-408f-92d6-d2be4da516cb", 1618500419, "ADMIN"
                    )
                )
        )
    }

    @Test
    fun login_error() {
        doReturn(mockRequestBody).`when`(mockLoginRepository).makeJsonObject(anyString(), anyString())
        doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIUser::class.java)
        doReturn(mockCall).`when`(mockRetrofit).loginUser(mockRequestBody)

        val response = "{error: \"Internal Server Error\", message:\"\", path:\"/ api/account/login\",status=400, timestamp:\"2021-04-13T15:39:37.015+00:00\"}"
        val errorResponse = response.toResponseBody("application/json".toMediaTypeOrNull())

        doAnswer { invocation ->
            val callback: Callback<TokenAuthorities> = invocation.getArgument(0)
            callback.onResponse(mockCall, Response.error(400, errorResponse))
            null
        }.`when`(mockCall).enqueue(any())

        mockLoginRepository.login("admin", "password")
        assertTrue(
            mockLoginRepository.serverResponse.value?.peekContent() ==
                Result.Error(exception = "Internal Server Error")
        )
    }

    @Test
    fun login_exception() {
        doReturn(mockRequestBody).`when`(mockLoginRepository).makeJsonObject(anyString(), anyString())
        doReturn(mockRetrofit).`when`(mockNetworkClient).buildService(APIUser::class.java)
        doReturn(mockCall).`when`(mockRetrofit).loginUser(mockRequestBody)

        doAnswer { invocation ->
            val callback: Callback<TokenAuthorities> = invocation.getArgument(0)
            callback.onFailure(mockCall, TimeoutException("Timeout"))
            null
        }.`when`(mockCall).enqueue(any())

        mockLoginRepository.login("admin", "password")
        assertTrue(
            mockLoginRepository.serverResponse.value?.peekContent() ==
                Result.Error(exception = "Timeout")
        )
    }
}

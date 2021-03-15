package com.sweven.blockcovid.services

import android.content.Context
import com.sweven.blockcovid.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.security.KeyStore
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


object ServiceGenerator {

    /**
     * Generates an OkHttpClient with our trusted CAs
     * to make calls to a service which requires it.
     *
     * @param context the context to access our file.
     * @return OkHttpClient with our trusted CAs added.
     */

    private fun generateSecureOkHttpClient(context: Context): OkHttpClient {
        // Create a simple builder for our http client, this is only por example purposes
        val httpClientBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        // Here you may wanna add some headers or custom setting for your builder

        // Get the file of our certificate
        val caFileInputStream = context.resources.openRawResource(R.raw.my_certificate)

        // We're going to put our certificates in a Keystore
        val keyStore = KeyStore.getInstance("PKCS12")
        keyStore.load(caFileInputStream, "my file password".toCharArray())

        // Create a KeyManagerFactory with our specific algorithm our our public keys
        // Most of the cases is gonna be "X509"

        val keyManagerFactory = KeyManagerFactory.getInstance("X509")
        keyManagerFactory.init(keyStore, "my file password".toCharArray())

        // Create a SSL context with the key managers of the KeyManagerFactory
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(keyManagerFactory.keyManagers, null, SecureRandom())

        //crea trust manager

        val trustManagerFactory: TrustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)

        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + trustManagers.contentToString()
        }

        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager
        sslContext.init(null, arrayOf(trustManager), null)

        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        //Finally set the sslSocketFactory to our builder and build it

        return httpClientBuilder
            .sslSocketFactory(sslSocketFactory, trustManager)
            .build()
    }

    /**
     * Example method to show the usage of the OkHttpClient
     *
     * @param context the context to access our file.
     * @return Retrofit service
     */

    fun generateService(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://development.myapi.com/")
            .client(generateSecureOkHttpClient(context))
            .build()
    }
}
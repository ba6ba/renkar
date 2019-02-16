package com.example.sarwan.renkar.network

import android.annotation.SuppressLint
import android.app.Activity
import android.provider.Settings
import android.util.Log
import com.example.sarwan.renkar.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException
import javax.security.cert.X509Certificate


object RestClient {

    private val TIMEOUT = 25
    private var logging: HttpLoggingInterceptor? = null
    private val TAG: String = "RESTCLIENT"


    init {
        setupClient()
    }

    private fun setupClient() {

        setupLogging()
    }

    private fun setupLogging() {
        logging = HttpLoggingInterceptor()
        logging?.level = if ( BuildConfig.DEBUG ) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    fun getRestAdapter(url : String): WebServices {

        val httpClient = getUnsafeOkHttpClient()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val newRequest: Request
                    newRequest = request.newBuilder()
                            .addHeader("Accept","application/json")
                            .build()
                    chain.proceed(newRequest)
                }
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder().
            baseUrl(url).
            addConverterFactory(GsonConverterFactory.create(gson)).
            client(httpClient).
            build()

        return retrofit.create<WebServices>(WebServices::class.java)
    }

    fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {

                }

                override fun checkServerTrusted(
                    chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return emptyArray()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(activity: Activity): String {
        return try {
            Settings.Secure.getString(activity.contentResolver,
                Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            ""
        }
    }
}

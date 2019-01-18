package com.example.sarwan.renkar.network

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
import java.util.concurrent.TimeUnit


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
        logging!!.level = if ( BuildConfig.DEBUG ) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }


    fun getRestAuthenticatedAdapter(token: String?): WebServices {

        val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val newRequest: Request
                    newRequest = request.newBuilder()
                            .addHeader("Authorization",token)
                            .addHeader("Content-Type","application/json")
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
                       baseUrl(NetworkConstants.BASE_URL).
                       addConverterFactory(GsonConverterFactory.create(gson)).
                       client(httpClient).
                        build()

        return retrofit.create<WebServices>(WebServices::class.java)
    }

    fun getRestAdapter(): WebServices {

        val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val newRequest: Request
                    newRequest = request.newBuilder()
                            .addHeader("Content-Type","application/json")
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
                baseUrl(NetworkConstants.BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                client(httpClient).
                build()

        return retrofit.create<WebServices>(WebServices::class.java!!)
    }

    private fun getDeviceId(activity: Activity): String {
        try {
            return Settings.Secure.getString(activity.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (e: Exception)
        {
            Log.d(TAG,e.toString())
            return ""
        }
    }
}

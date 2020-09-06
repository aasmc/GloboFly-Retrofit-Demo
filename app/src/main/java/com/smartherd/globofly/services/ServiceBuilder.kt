package com.smartherd.globofly.services

import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object ServiceBuilder {
    /**
     * Base URL of the server
     */
    private const val URL = "http://10.0.2.2:9000/"

    // create interceptor
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    // create header interceptor - anonymous implementation of Interceptor interface
    // that has a method intercept(chain: Interceptor.Chain) that we pass as a lambda
    val headerInterceptor = Interceptor { chain ->
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("x-device-type", Build.DEVICE)
            .addHeader("Accept-Language", Locale.getDefault().language)
            .build()
        // returns the response
        chain.proceed(request)
    }

    /**
     * OkHttp client for the server
     * Header interceptor must coe before the logging interceptor
     */
    private val okHttp =
        OkHttpClient.Builder()
            .callTimeout(15, TimeUnit.SECONDS) // adds timeout for calling to the server
            .addInterceptor(headerInterceptor)
            .addInterceptor(logger)

    /**
     * Retrofit builder based off of the base URL
     * adds GsonConverterFactory and okHttp client
     */
    private val builder = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    /**
     * Instance of retrofit
     */
    private val retrofit = builder.build()

    /**
     * Creates retrofit service of type T that needs to be an implementation of in our case
     * DestinationService interface to enable using server functions
     */
    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}
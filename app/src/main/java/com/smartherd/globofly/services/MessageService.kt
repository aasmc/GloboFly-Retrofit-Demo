package com.smartherd.globofly.services

import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Interface that defines a method to retrieve response from a different URL
 */
interface MessageService {

    /**
     * Retrieves a message from server at specified URL
     * @param anotherUrl location of the server
     */
    @GET
    suspend fun getMessages(@Url anotherUrl: String) : String
}
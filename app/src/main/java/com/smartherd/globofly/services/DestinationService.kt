package com.smartherd.globofly.services

import com.smartherd.globofly.models.Destination
import retrofit2.Call
import retrofit2.http.*

interface DestinationService {

    /**
     * Retrieves list of destinations from the server
     * @param filter QueryMap (HashMap) of string-> string key value pairs used for filtering the response
     */
    @GET("destination")
    suspend fun getDestinationList(
        @QueryMap filter: HashMap<String, String>,
    ): List<Destination>

    /**
     * Functions that GETs a destination from the server by its id
     * Need to specify the path in @GET("") and add id in curly braces to allow for dynamic insertion
     * Need to annotate function parameter as @Path("id")
     */
    @GET("destination/{id}")
    suspend fun getDestination(@Path("id") id: Int): Destination


    /**
     * Adds a destination to the server in the form of JSON object
     */
    @POST("destination")
    suspend fun addDestination(@Body newDestination: Destination): Destination

    /**
     * Updates the destination ad the specified id in the server using Form Url Encoded format
     * Field parameters must match those stored in the server
     */
    @FormUrlEncoded
    @PUT("destination/{id}")
    suspend fun updateDestination(
        @Path("id") id: Int,
        @Field("city") city: String,
        @Field("description") desc: String,
        @Field("country") country: String
    ): Destination

    /**
     * Deletes a destination from the server by destination Id
     */
    @DELETE("destination/{id}")
    fun deleteDestination(@Path("id") id: Int): Call<Unit>
}
package com.machinarium.sbs.network

import com.machinarium.sbs.request.AuthRequest
import com.machinarium.sbs.request.CreateStreamRequest
import com.machinarium.sbs.response.basics.AuthResponse
import com.machinarium.sbs.response.basics.ConfigResponse
import com.machinarium.sbs.response.stream.CreateStreamResponse
import com.machinarium.sbs.response.stream.StreamListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("config")
    suspend fun getConfig(): ConfigResponse

    @POST("stream")
    suspend fun createStream(@Body request: CreateStreamRequest): CreateStreamResponse

    @GET("streams")
    suspend fun getStreams(): StreamListResponse

    @DELETE("stream/{id}")
    suspend fun stopStream(@Path("id") streamId: Long)
}
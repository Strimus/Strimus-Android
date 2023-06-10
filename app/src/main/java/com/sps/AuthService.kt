package com.sps

import com.machinarium.sbs.request.AuthRequest
import com.machinarium.sbs.request.RegisterRequest
import com.machinarium.sbs.response.basics.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("fake/auth")
    suspend fun auth(@Body request: AuthRequest): AuthResponse

    @POST("fake/streamer")
    suspend fun streamer(@Body request: RegisterRequest): AuthResponse
}
package com.machinarium.sbs.network

import com.machinarium.sbs.model.stream.StreamData
import com.machinarium.sbs.request.CreateStreamRequest
import com.machinarium.sbs.response.basics.ConfigResponse
import com.machinarium.sbs.response.stream.CreateStreamResponse
import com.machinarium.sbs.response.stream.StreamListResponse

interface StreamRepository {

    suspend fun getConfig(): Result<ConfigResponse>

    suspend fun createStream(
        source: String,
        streamData: StreamData
    ): Result<CreateStreamResponse>

    suspend fun getStreams(): Result<StreamListResponse>

    suspend fun stopStream(streamId: Long): Result<Unit>

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Result<T> {
        return try {
            Result.success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}

class StreamRepositoryImpl(private val apiService: ApiService) : StreamRepository {

    override suspend fun getConfig() = safeApiCall {
        apiService.getConfig()
    }

    override suspend fun createStream(
        source: String,
        streamData: StreamData
    ): Result<CreateStreamResponse> = safeApiCall {
        apiService.createStream(CreateStreamRequest(source, streamData))
    }


    override suspend fun getStreams(): Result<StreamListResponse> = safeApiCall {
        apiService.getStreams()
    }

    override suspend fun stopStream(streamId: Long): Result<Unit> = safeApiCall {
        apiService.stopStream(streamId)
    }
}
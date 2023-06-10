package com.machinarium.sbs

import android.util.Log
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.machinarium.sbs.model.init.Config
import com.machinarium.sbs.model.stream.StreamData
import com.machinarium.sbs.model.stream.StreamItem
import com.machinarium.sbs.network.ApiService
import com.machinarium.sbs.request.AuthRequest
import com.machinarium.sbs.request.CreateStreamRequest
import com.machinarium.sbs.response.basics.AuthResponse
import com.machinarium.sbs.response.stream.CreateStreamResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SPSManager {

    private var config: Config? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var retrofit: Retrofit? = null
    private var uniqueId: String = ""

    private var streamId: Long? = null

    private val service by lazy { retrofit?.create(ApiService::class.java) }

    fun initialize(token: String, key: String, uniqueId: String = "") {
        this.uniqueId = uniqueId
        retrofit = Retrofit.Builder()
            .baseUrl("http://164.92.178.132:5555/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient(token, key))
            .build()


        coroutineScope.launch {
            try {
                val response = service?.getConfig()
                response?.apply {
                    if (success && code == 200) {
                        config = data
                    }
                }
            } catch (e: Exception) {
                Log.e(SPS.TAG, e.message ?: "")
            }
        }
    }

    private fun buildHttpClient(token: String, key: String): OkHttpClient {
        val prettyLogInterceptor = LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Pretty-Request")
            .response("Pretty-Response")
            .build()

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .build()
            val request = original.newBuilder()
                .addHeader(TOKEN, token)
                .addHeader(PARTNER_KEY, key)
                .url(url).build()
            chain.proceed(request)
        }
        okHttpClientBuilder.addInterceptor(prettyLogInterceptor)

        return okHttpClientBuilder.build()
    }

    suspend fun createStream(): CreateStreamResponse? {
        val source = config?.configs?.find { configItem -> configItem.default }?.alias ?: "aws"
        val request = CreateStreamRequest(
            source,
            StreamData(this.uniqueId, "Test Stream", "https://picsum.photos/500/500")
        )
        return service?.createStream(request).also {
            streamId = it?.data?.id
        }
    }

    suspend fun getStreams(onLisReceived: (List<StreamItem>) -> Unit) {
        coroutineScope.launch {
            val response = service?.getStreams()
            response?.apply {
                if (success && code == 200) {
                    onLisReceived.invoke(data)
                }
            }
        }
    }

    suspend fun stopStream() {
        val id = streamId ?: return
        coroutineScope.launch {
            try {
                service?.stopStream(id)
            } catch (e: Exception) {
                Log.e(SPS.TAG, e.message ?: "")
            }
        }
    }

    companion object {
        private const val TOKEN = "token"
        private const val PARTNER_KEY = "key"
    }
}
package com.sps

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.machinarium.sbs.SPS
import com.machinarium.sbs.request.AuthRequest
import com.machinarium.sbs.request.RegisterRequest
import com.sps.databinding.ActivitySplashBinding
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var prefs: SharedPreferences

    var userId: String?
        set(value) {
            with(prefs.edit()) {
                putString(USER_ID, value)
                apply()
            }
        }
        get() {
            return prefs.getString(USER_ID, null)
        }

    companion object {
        const val USER_INFO = "USER_INFO"
        const val USER_ID = "userId"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        prefs = this.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)

        if (userId.isNullOrEmpty().not()) {
            requestAuth(userId!!)
        } else {
            createUser()
        }

        binding.refreshUser.setOnClickListener {
            binding.next.isEnabled = false
            requestAuth(createUser())
        }

        binding.next.setOnClickListener {
            startActivity(Intent(this@SplashActivity, SBS::class.java))
        }
    }

    private fun requestAuth(userId: String) {
        binding.userId.text = userId
        val authRequest = AuthRequest(
            "test_key",
            "test_secret",
            userId
        )
        val retrofit = Retrofit.Builder()
            .baseUrl("http://164.92.178.132:5555/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                try {
                    val service = retrofit.create(AuthService::class.java)
                    val response = service.auth(authRequest)
                    response.apply {
                        if (success && code == 200) {
                            SPS.getInstance().initialize(data.token, "test_key", userId)
                            binding.next.isEnabled = true
                        }
                    }


                } catch (e: Exception) {
                    Log.e(SPS.TAG, e.message ?: "")
                }
            }
        }
    }

    private fun requestStreamer(userId: String) {
        val number = (0..100).random()
        val registerRequest = RegisterRequest(
            "test_key",
            "test_secret",
            RegisterRequest.User(
                userId,
                "Fatih $number. Mehmet",
                "https://picsum.photos/500/500",
                "fatih$number@ottoman.com",
                "https://linkedin.com/fatih$number"
            )
        )
        val retrofit = Retrofit.Builder()
            .baseUrl("http://164.92.178.132:5555/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                try {
                    retrofit.create(AuthService::class.java).streamer(registerRequest)
                } catch (e: Exception) {
                    Log.e(SPS.TAG, e.message ?: "")
                }
            }
        }
    }

    private fun buildHttpClient(): OkHttpClient {
        val prettyLogInterceptor = LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Pretty-Request")
            .response("Pretty-Response")
            .build()

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(prettyLogInterceptor)

        return okHttpClientBuilder.build()
    }

    private fun createUser(): String {
        val timeStamp: String = java.lang.String.valueOf(
            TimeUnit.MILLISECONDS.toSeconds(
                System.currentTimeMillis()
            )
        )
        userId = timeStamp
        requestStreamer(timeStamp)
        return timeStamp
    }
}
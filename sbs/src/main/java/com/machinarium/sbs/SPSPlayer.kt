package com.machinarium.sbs

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.amazonaws.ivs.player.*
import com.amazonaws.ivs.player.Player.Listener


class SPSPlayer(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    DefaultLifecycleObserver {
    private val playerView: PlayerView
    private val player: Player

    private var streamUri: Uri? = null

    init {
        // Create a player instance
        playerView = PlayerView(context)
        addView(playerView)
        player = playerView.player
        player.addListener(object : Listener() {
            override fun onCue(p0: Cue) {

            }

            override fun onDurationChanged(p0: Long) {

            }

            override fun onStateChanged(state: Player.State) {
                when (state) {
                    Player.State.BUFFERING -> {}
                    Player.State.READY -> player.play()
                    Player.State.IDLE -> {}
                    Player.State.PLAYING -> {}
                    Player.State.ENDED -> {}
                }
            }

            override fun onError(p0: PlayerException) {

            }

            override fun onRebuffering() {

            }

            override fun onSeekCompleted(p0: Long) {

            }

            override fun onVideoSizeChanged(p0: Int, p1: Int) {

            }

            override fun onQualityChanged(p0: Quality) {

            }
        })
    }

    fun play(uri: Uri) {

        player.play()
    }

    fun pause() {
        player.release()
    }

    fun endBroadcast() {
        removeAllViews()
        player.release()
    }

    fun setUri(uri: Uri): SPSPlayer {
        streamUri = uri
        return this
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        streamUri?.let {
            player.load(it)
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        player.play()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        player.pause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        if (player.state == Player.State.PLAYING) {
            player.pause()
        }
        player.release()
    }
}
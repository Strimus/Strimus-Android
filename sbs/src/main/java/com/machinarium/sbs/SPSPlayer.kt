package com.machinarium.sbs

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import com.amazonaws.ivs.player.*
import com.amazonaws.ivs.player.Player.Listener


class SPSPlayer(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val playerView: PlayerView
    private val player: Player

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
        player.load(uri)
        player.play()
    }

    fun pause() {
        player.release()
    }

    fun endBroadcast() {
        removeAllViews()
        player.release()
    }
}
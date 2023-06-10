package com.machinarium.sbs

import android.Manifest.permission.CAMERA
import android.Manifest.permission.RECORD_AUDIO
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresPermission
import com.amazonaws.ivs.broadcast.*
import com.machinarium.sbs.recorder.AWSRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SPSRecordPlayer(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

//    private val broadcastSession: BroadcastSession
//    private val broadcastListener: BroadcastSession.Listener

    private var recorder: SPSRecordInterface? = null

    @RequiresPermission(allOf = [RECORD_AUDIO, CAMERA])
    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = SPS.getInstance().createStream()
            response?.apply {
                if (success) {
                    recorder = AWSRecorder(this@SPSRecordPlayer)
                    recorder?.start(data)
//                    broadcastSession.awaitDeviceChanges {
//                        if (broadcastSession.isReady) {
//                            broadcastSession.start(data.streamUrl, data.streamKey)
//                        }
//                    }
                }
            }
        }
    }

    fun stop() {
//        broadcastSession.stop()
    }

    fun endBroadcast() {
        removeAllViews()
//        broadcastSession.release()
    }

    init {
        keepScreenOn = true
//        broadcastListener = object : BroadcastSession.Listener() {
//            override fun onError(p0: BroadcastException) {
//                println("HHHHHHH ${p0.detail}")
//            }
//
//            override fun onStateChanged(p0: BroadcastSession.State) {
//                println("HHHHHHH ${p0.name}")
//            }
//        }
//        broadcastSession = BroadcastSession(
//            context,
//            broadcastListener,
//            Presets.Configuration.STANDARD_PORTRAIT,
//            Presets.Devices.FRONT_CAMERA(context)
//        )
//
//        broadcastSession.awaitDeviceChanges {
//            for (device in broadcastSession.listAttachedDevices()) {
//                // Find the camera we attached earlier
//                if (device.descriptor.type === Device.Descriptor.DeviceType.CAMERA) {
//                    val preview = (device as ImageDevice).previewView
//                    preview.layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT
//                    )
//                    addView(preview)
//                }
//            }
//        }
    }
}
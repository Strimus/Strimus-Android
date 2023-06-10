package com.machinarium.sbs.recorder

import android.view.ViewGroup
import android.widget.LinearLayout
import com.amazonaws.ivs.broadcast.*
import com.machinarium.sbs.SPSRecordInterface
import com.machinarium.sbs.model.stream.CreateStream

class AWSRecorder(parent: ViewGroup) : SPSRecordInterface {

    private val broadcastSession: BroadcastSession
    private val broadcastListener: BroadcastSession.Listener

    init {
        broadcastListener = object : BroadcastSession.Listener() {
            override fun onError(p0: BroadcastException) {
                println("HHHHHHH ${p0.detail}")
            }

            override fun onStateChanged(p0: BroadcastSession.State) {
                println("HHHHHHH ${p0.name}")
            }
        }
        broadcastSession = BroadcastSession(
            parent.context,
            broadcastListener,
            Presets.Configuration.STANDARD_PORTRAIT,
            Presets.Devices.FRONT_CAMERA(parent.context)
        )

        broadcastSession.awaitDeviceChanges {
            for (device in broadcastSession.listAttachedDevices()) {
                // Find the camera we attached earlier
                if (device.descriptor.type === Device.Descriptor.DeviceType.CAMERA) {
                    val preview = (device as ImageDevice).previewView
                    preview.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    parent.addView(preview)
                }
            }
        }
    }


    override fun start(data: CreateStream) {
        broadcastSession.awaitDeviceChanges {
            if (broadcastSession.isReady) {
                broadcastSession.start(data.streamUrl, data.streamKey)
            }
        }
    }

    override fun stop() {
        broadcastSession.stop()
    }

    override fun end() {
        broadcastSession.release()
    }
}
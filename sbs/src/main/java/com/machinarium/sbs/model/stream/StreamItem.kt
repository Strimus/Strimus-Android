package com.machinarium.sbs.model.stream

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class StreamItem(
    val streamData: StreamData,
    val url: String,
    val type: String,
    val id: Int,
    val videos: ArrayList<Video>?,
): Parcelable

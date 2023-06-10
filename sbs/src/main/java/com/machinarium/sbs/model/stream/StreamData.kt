package com.machinarium.sbs.model.stream

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StreamData(val uniqueId: String, val name: String, val image: String): Parcelable

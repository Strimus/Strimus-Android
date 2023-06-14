package com.machinarium.sbs.model.init

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfigItem(
    val id: String, val label: String, val alias: String, val default: Boolean
) : Parcelable
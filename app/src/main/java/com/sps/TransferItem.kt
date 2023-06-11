package com.sps

import android.os.Parcelable
import com.machinarium.sbs.model.stream.StreamItem

@kotlinx.parcelize.Parcelize
class TransferItem(val list: List<StreamItem>, val id: Int) : Parcelable
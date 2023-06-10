package com.machinarium.sbs.request

import com.machinarium.sbs.model.stream.StreamData

data class CreateStreamRequest(
    val source: String, val streamData: StreamData
)
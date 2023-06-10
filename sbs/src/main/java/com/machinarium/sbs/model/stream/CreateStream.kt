package com.machinarium.sbs.model.stream

class CreateStream(
    val id: Long,
    val streamData: StreamData,
    val streamUrl: String,
    val streamKey: String,
    val source: String
)

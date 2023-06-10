package com.machinarium.sbs

import com.machinarium.sbs.model.stream.CreateStream

interface SPSRecordInterface {

    fun start(data: CreateStream)

    fun stop()

    fun end()
}
package com.machinarium.sbs.response.stream

import com.machinarium.sbs.model.init.Config
import com.machinarium.sbs.model.stream.CreateStream
import com.machinarium.sbs.model.stream.StreamItem
import com.machinarium.sbs.response.BaseResponse

class StreamListResponse(val data: List<StreamItem>) : BaseResponse()

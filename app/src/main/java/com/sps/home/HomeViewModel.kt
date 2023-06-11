package com.sps.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.machinarium.sbs.SPS
import com.machinarium.sbs.model.stream.StreamItem
import com.machinarium.sbs.response.stream.StreamListResponse
import com.sps.home.adapter.StreamListAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState: StateFlow<HomeViewState> get() = _viewState

    fun loadStreamList() {
        viewModelScope.launch {
            SPS.getInstance().getStreams().fold(
                onSuccess = {
                    handleStreamsResponse(it)
                },
                onFailure = {

                }
            )
        }
    }

    private fun handleStreamsResponse(response: StreamListResponse) {
        val liveStreams = response.data.filter { it.isLiveStream() }
        val oldStreams = response.data.filter { !it.isLiveStream() }

        _viewState.value = HomeViewState(
            liveStreams = liveStreams,
            oldStreams = oldStreams
        )
    }
}


data class HomeViewState(
    val liveStreams: List<StreamItem>? = null,
    val oldStreams: List<StreamItem>? = null,
) {

    val listItems
        get() : List<StreamListAdapter.ListItem> {
            return buildList {
                liveStreams?.let {
                    if (it.isNotEmpty()) {
                        add(StreamListAdapter.ListItem.Title("Live Streams"))
                    }

                    addAll(it.map { StreamListAdapter.ListItem.Item(it) })
                }

                oldStreams?.let {
                    if (it.isNotEmpty()) {
                        add(StreamListAdapter.ListItem.Title("Past Streams"))
                    }

                    addAll(it.map { StreamListAdapter.ListItem.Item(it) })
                }
            }
        }
}
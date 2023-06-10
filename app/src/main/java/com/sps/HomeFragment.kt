package com.sps

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.machinarium.sbs.SPS
import com.machinarium.sbs.model.stream.StreamItem
import com.sps.databinding.FragmentHomeBinding
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val mainList = arrayListOf<StreamItem>()

    override fun getLayoutRes() = R.layout.fragment_home

    private val adapter = ListAdapter {
        val bundle = bundleOf(PagerFragment.KEY_ITEM to TransferItem(mainList, it))
        findNavController().navigate(R.id.navigateLive, bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.apply {
            this.adapter = this@HomeFragment.adapter
        }

        binding.startBroadcast.setOnClickListener {
            findNavController().navigate(R.id.navigateBroadcast)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    SPS.getInstance().getStreams { mainList ->
                        this@HomeFragment.mainList.clear()
                        this@HomeFragment.mainList.addAll(mainList)
                        val liveList = mutableListOf<StreamItem>()
                        val oldList = mutableListOf<StreamItem>()
                        mainList.forEach { listItem->
                            if (listItem.type == "live") {
                                liveList.add(listItem)
                            } else if (listItem.videos.isNullOrEmpty().not()) {
                                oldList.add(listItem)
                            }
                        }
                        val list = mutableListOf<ListAdapter.Item>()
                        if (liveList.isNotEmpty()) {
                            list.add(ListAdapter.Item("Live Streams"))
                            liveList.forEach {
                                list.add(ListAdapter.Item(item = it))
                            }
                        }
                        if (oldList.isNotEmpty()) {
                            list.add(ListAdapter.Item("Past Streams"))
                            oldList.forEach {
                                list.add(ListAdapter.Item(item = it))
                            }
                        }
                        adapter.list = list
                    }
                }
            }
        }
    }
}
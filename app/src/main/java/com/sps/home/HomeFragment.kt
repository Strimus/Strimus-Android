package com.sps.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.machinarium.sbs.SPS
import com.machinarium.sbs.model.stream.StreamItem
import com.sps.BaseFragment
import com.sps.home.adapter.StreamListAdapter
import com.sps.PagerFragment
import com.sps.R
import com.sps.TransferItem
import com.sps.broadcast.broadcastoptions.BroadcastOptionsBottomSheetDialogFragment
import com.sps.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val mainList = arrayListOf<StreamItem>()

    private val viewModel by viewModels<HomeViewModel>()

    override fun getLayoutRes() = R.layout.fragment_home

    private val adapter = StreamListAdapter(::onStreamItemClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rv.apply {
            this.adapter = this@HomeFragment.adapter
        }

        binding.startBroadcast.setOnClickListener {
            checkBroadcastOptionsAndNavigate()
        }

        viewModel.loadStreamList()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.viewState.collect {
                        adapter.submitList(it.listItems)
                    }
                }
            }
        }
    }

    private fun checkBroadcastOptionsAndNavigate() {
        val avaibleSources = SPS.getInstance().getAvailableBroadcastSources()
        val sourceSize = avaibleSources.size


        when (sourceSize) {
            0 -> {
                Toast.makeText(requireContext(), "No broadcast sources found", Toast.LENGTH_SHORT)
                    .show()
            }

            1 -> {
                val source = avaibleSources.firstOrNull() ?: return
                findNavController().navigate(HomeFragmentDirections.navigateBroadcast(source))
            }

            else -> {
                BroadcastOptionsBottomSheetDialogFragment.newInstance {
                    findNavController().navigate(HomeFragmentDirections.navigateBroadcast(it))
                }.show(childFragmentManager, BroadcastOptionsBottomSheetDialogFragment.TAG)
            }
        }
    }

    private fun onStreamItemClick(item: StreamItem) {
        val viewState = viewModel.viewState.value
        val isLiveStream = item.isLiveStream()

        val streamList = if (isLiveStream) {
            viewState.liveStreams
        } else {
            viewState.oldStreams
        } ?: return

        val streamIndex = streamList.indexOf(item)

        val bundle = bundleOf(PagerFragment.KEY_ITEM to TransferItem(streamList, streamIndex))
        findNavController().navigate(R.id.navigateLive, bundle)
    }
}
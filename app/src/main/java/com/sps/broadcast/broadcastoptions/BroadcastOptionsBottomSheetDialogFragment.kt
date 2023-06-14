package com.sps.broadcast.broadcastoptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.machinarium.sbs.SPS
import com.machinarium.sbs.model.init.ConfigItem
import com.sps.R
import com.sps.broadcast.broadcastoptions.adapter.BroadcastOptionsAdapter
import com.sps.databinding.FragmentBottomSheetBroadcastOptionsBinding

class BroadcastOptionsBottomSheetDialogFragment :
    BottomSheetDialogFragment(R.layout.fragment_bottom_sheet_broadcast_options) {

    private var _binding: FragmentBottomSheetBroadcastOptionsBinding? = null
    val binding get() = _binding!!

    private var selectionListener: ((ConfigItem) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBroadcastOptionsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAdapter().submitList(SPS.getInstance().getAvailableBroadcastSources())
    }

    private fun getAdapter() =
        (binding.rvOptions.adapter as? BroadcastOptionsAdapter) ?: kotlin.run {
            BroadcastOptionsAdapter {
                selectionListener?.invoke(it)
                dismiss()
            }.also {
                binding.rvOptions.adapter = it
            }
        }

    fun setSelectionListener(selectionListener: (ConfigItem) -> Unit) {
        this.selectionListener = selectionListener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BroadcastOptionsBottomSheetDialogFragment"

        fun newInstance(selectionListener: (ConfigItem) -> Unit) =
            BroadcastOptionsBottomSheetDialogFragment().apply {
                setSelectionListener(selectionListener)
            }
    }
}
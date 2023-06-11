package com.sps

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.sps.databinding.FragmentLiveBinding

class LiveFragment : BaseFragment<FragmentLiveBinding>() {
    override fun getLayoutRes() = R.layout.fragment_live

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString(ARG_KEY_URL) ?: return
        val index = arguments?.getInt(ARG_KEY_INDEX) ?: return

        binding.recorder
            .setUri(Uri.parse(url))
            .setLifecycleOwner(viewLifecycleOwner)
    }

    companion object {

        private const val ARG_KEY_URL = "argKeyUrl"
        private const val ARG_KEY_INDEX = "argKeyIndex"
        fun newInstance(url: String?, index: Int) =
            LiveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_KEY_URL, url)
                    putInt(ARG_KEY_INDEX, index)
                }
            }
    }
}
package com.sps

import android.icu.text.Transliterator.Position
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.sps.databinding.FragmentLiveBinding

class LiveFragment : BaseFragment<FragmentLiveBinding>() {
    var url: String = ""
    var index: Int = 0

    companion object {
        fun newInstance(url: String, index: Int): LiveFragment {
            val fragment = LiveFragment()
            fragment.url = url
            fragment.index = index
            return fragment
        }
    }

    override fun getLayoutRes() = R.layout.fragment_live


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
//        println("HHHH onResume $index")
        binding.recorder.play(Uri.parse(url))
    }


    override fun onPause() {
        super.onPause()
//        println("HHHH onPause $index")
        binding.recorder.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
//        println("HHHH onDestroy $index")
        binding.recorder.endBroadcast()
    }
}
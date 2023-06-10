package com.sps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<ViewBinding : ViewDataBinding> : Fragment() {

    lateinit var binding: ViewBinding

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (!this::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
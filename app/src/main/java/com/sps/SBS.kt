package com.sps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.machinarium.sbs.SPS
import com.sps.databinding.ActivitySbsBinding


class SBS : AppCompatActivity() {
    lateinit var binding: ActivitySbsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sbs)
    }
}
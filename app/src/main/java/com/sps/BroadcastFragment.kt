package com.sps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sps.databinding.FragmentBroadcastBinding

class BroadcastFragment : BaseFragment<FragmentBroadcastBinding>() {
    override fun getLayoutRes() = R.layout.fragment_broadcast

    private val requiredPermissions =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(requireActivity(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // If any permissions are missing we want to just request them all.
                requestPermissions(requiredPermissions, 10000)
                break
            }
        }
        setupBroadcastSession()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10000) {
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(
                        requireContext(),
                        "Permissions required to run",
                        Toast.LENGTH_LONG
                    ).show()
                    requireActivity().onBackPressed()
                    return
                }
            }
            setupBroadcastSession()
        }
    }

    private fun setupBroadcastSession() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.recorder.start()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.recorder.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recorder.endBroadcast()
    }
}
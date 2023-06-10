package com.machinarium.sbs

class SPS {
    companion object {
        const val TAG = "SBS"
        private var _SPSManager: SPSManager? = null

        fun getInstance(): SPSManager {
            _SPSManager?.let {
                return it
            } ?: kotlin.run {
                _SPSManager = SPSManager()
                return _SPSManager!!
            }
        }
    }
}
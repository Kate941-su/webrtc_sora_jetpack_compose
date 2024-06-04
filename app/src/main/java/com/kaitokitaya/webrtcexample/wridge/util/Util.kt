package com.wridge.util

import android.util.Log

class Util {
    companion object {
        fun debugPrint(anyObject: Any) {
            Log.d("Console Print", anyObject.toString())
        }
    }
}

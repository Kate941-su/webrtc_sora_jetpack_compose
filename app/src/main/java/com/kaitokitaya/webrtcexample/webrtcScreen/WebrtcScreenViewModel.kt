package com.kaitokitaya.webrtcexample.webrtcScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kaitokitaya.webrtcexample.CameraCaputurerHandler
import jp.shiguredo.sora.sdk.channel.option.SoraAudioOption
import jp.shiguredo.sora.sdk.channel.option.SoraChannelRole
import jp.shiguredo.sora.sdk.channel.option.SoraMediaOption
import jp.shiguredo.sora.sdk.channel.option.SoraVideoOption
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.audio.JavaAudioDeviceModule


class WebrtcScreenViewModel(var capturerHandler: CameraCaputurerHandler? = null) : ViewModel() {

    fun onStart() {
        Log.d("test", "test start")
        capturerHandler?.apply {
            if (capturer == null || eglBase == null) {
                return
            }
        }
        capturerHandler?.apply {
            setMediaChannelObject()
            connectSora()
        }
    }


    fun onStop() {
        capturerHandler?.disconnectSora()
    }

    companion object {
        private val TAG = WebrtcScreenViewModel::class.java.simpleName
    }

}
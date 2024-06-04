package com.kaitokitaya.webrtcexample

import android.util.Log
import com.kaitokitaya.webrtcexample.webrtcScreen.WebrtcScreenViewModel
import jp.shiguredo.sora.sdk.BuildConfig
import jp.shiguredo.sora.sdk.channel.SoraMediaChannel
import jp.shiguredo.sora.sdk.channel.option.PeerConnectionOption
import jp.shiguredo.sora.sdk.channel.option.SoraAudioOption
import jp.shiguredo.sora.sdk.channel.option.SoraChannelRole
import jp.shiguredo.sora.sdk.channel.option.SoraMediaOption
import jp.shiguredo.sora.sdk.channel.option.SoraVideoOption
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.EglBase
import org.webrtc.MediaStream
import org.webrtc.SurfaceViewRenderer
import org.webrtc.audio.JavaAudioDeviceModule

class CameraCaputurerHandler(private val activity: MainActivity) {
    private val timeOut = 5000L
    var capturer: CameraVideoCapturer? = null
    val eglBase: EglBase? = EglBase.create()
    var mediaChannel: SoraMediaChannel? = null
    val surfaceViewRenderer = SurfaceViewRenderer(activity)

    init {
        capturer ?: let {
            val enumerator = Camera2Enumerator(activity)
            val deviceNames = enumerator.deviceNames
            Log.d(TAG, deviceNames.toString())
            capturer = enumerator.createCapturer(deviceNames[0], null)
            if (eglBase?.eglBaseContext != null) {
                surfaceViewRenderer.init(eglBase.eglBaseContext, null)
            }
        }
    }

    fun setMediaChannelObject() {
        val option =
            SoraMediaOption().apply {
                enableAudioDownstream()
                enableAudioUpstream()
                enableMultistream()
                audioOption = SoraAudioOption()
                role = SoraChannelRole.SENDRECV
                videoCodec = SoraVideoOption.Codec.H264
                videoBitrate = 10000
                enableVideoUpstream(
                    capturer!!,
                    eglBase?.eglBaseContext,
                ) // todo ビデオを無効にするとSora接続直後に接続が切れてしまう
                enableMultistream()
            }
        mediaChannel =
            SoraMediaChannel(
                context = activity,
                peerConnectionOption =
                PeerConnectionOption().apply {
                    getStatsIntervalMSec = 1000
                },
                channelId = BuildConfig.CHANNEL_ID,
                clientId = BuildConfig.CLIENT_ID,
                mediaOption = option,
                signalingEndpoint = BuildConfig.SIGNALING_ENDPOINT,
                dataChannels =
                listOf(
                    mapOf(
                        "label" to "#set", "direction" to "sendonly",
                    ),
                    mapOf(
                        "label" to "#get", "direction" to "recvonly",
                    ),
                ),
                dataChannelSignaling = true,
                listener = object : SoraMediaChannel.Listener {
                    override fun onAddLocalStream(mediaChannel: SoraMediaChannel, ms: MediaStream) {
                        super.onAddLocalStream(mediaChannel, ms)
                        surfaceViewRenderer.apply {
                            capturer?.startCapture(
                                // TODO: Implement width and height from broadcast settings.
                                1280, 720,
                                30,
                            )
                            ms.also {
                                if (it.videoTracks.isNotEmpty()) {
                                    it.videoTracks[0].addSink(this)
                                }
                            }
                        }
                    }
                },
                // Only for Reconnect test
                timeoutSeconds = timeOut / 1000,
            )
    }

    fun connectSora() {
        mediaChannel?.connect()
    }

    fun disconnectSora() {
        Log.d("test", "test stop")
        mediaChannel?.disconnect()
        mediaChannel = null
        capturer?.stopCapture()
    }

    companion object {
        private val TAG = CameraCaputurerHandler::class.java.simpleName
    }


}
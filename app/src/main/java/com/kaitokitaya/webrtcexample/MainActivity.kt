package com.kaitokitaya.webrtcexample

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kaitokitaya.webrtcexample.ui.theme.WebrtcexampleTheme
import com.kaitokitaya.webrtcexample.webrtcScreen.WebrtcScreen
import com.kaitokitaya.webrtcexample.webrtcScreen.WebrtcScreenViewModel
import timber.log.Timber
import android.Manifest

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Timber.tag(TAG).d("Permission Granted")
        } else {
            Timber.tag(TAG).d("Permission Denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val capturerHandler = CameraCaputurerHandler(activity = this)
        val webRTCViewModel = WebrtcScreenViewModel(capturerHandler = capturerHandler)
        setContent {
            WebrtcexampleTheme {
                WebrtcScreen(viewModel = webRTCViewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Timber.tag(TAG).d("Permission has been already granted.")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                Timber.tag(TAG).d("Permission is being denied but this app has to have a grant.")
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WebrtcexampleTheme {
        Greeting("Android")
    }
}
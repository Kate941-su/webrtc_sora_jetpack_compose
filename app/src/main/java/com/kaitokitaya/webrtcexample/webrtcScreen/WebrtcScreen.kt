package com.kaitokitaya.webrtcexample.webrtcScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.webrtc.SurfaceViewRenderer

@Composable
fun WebrtcScreen(viewModel: WebrtcScreenViewModel) {
    SoraView(
        onStart = { viewModel.onStart() },
        onStop = { viewModel.onStop() },
        surfaceViewRenderer = viewModel.capturerHandler?.surfaceViewRenderer
    )
}

@Composable
fun SoraView(
    onStart: () -> Unit,
    onStop: () -> Unit,
    surfaceViewRenderer: SurfaceViewRenderer?,
) {
    Scaffold { innerPadding ->
        SurfaceViewCompose(surfaceViewRenderer = surfaceViewRenderer)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Button(onClick = onStart, modifier = Modifier.padding(12.dp)) {
                Text(text = "start", style = TextStyle(color = Color.White))
            }
            Button(onClick = onStop, modifier = Modifier.padding(12.dp)) {
                Text(text = "stop", style = TextStyle(color = Color.White))
            }
        }
    }
}

@Composable
fun SurfaceViewCompose(
    surfaceViewRenderer: SurfaceViewRenderer?
) {
    if (surfaceViewRenderer == null) {
        Box(modifier = Modifier
            .background(color = Color.Red)
            .fillMaxSize()) {
            Text(text = "surface view is null")
        }
    } else {
        AndroidView(
            factory = { _ ->
                surfaceViewRenderer
            },
        )        
    }
}
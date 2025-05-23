package com.ytone.longcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.ytone.longcare.theme.LongCareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LongCareTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Main content area
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp), // Add some padding around the column
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Greeting(
                            name = "Android User with Coil!",
                            modifier = Modifier.padding(bottom = 16.dp) // Add padding below Greeting
                        )

                        // Coil AsyncImage Example
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://picsum.photos/seed/picsum/400/300") // Example image URL
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery), // Placeholder from Android resources
                            error = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel), // Error placeholder from Android resources
                            contentDescription = stringResource(R.string.coil_image_description), // Add a string resource for this
                            modifier = Modifier.size(200.dp), // Specify a size for the image
                            contentScale = ContentScale.Crop // Adjust content scale as needed
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Example of the previously added Icon for A11y
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(id = R.string.icon_description_example),
                            modifier = Modifier.size(40.dp) // Give it a size
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Accompanist Permissions Example for Camera
                        CameraPermissionRequester()
                    }
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name", // Updated greeting text
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LongCareTheme {
        // Preview with a sample name
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
            Greeting("Coil User Preview")
            Spacer(modifier = Modifier.height(8.dp))
            // You can't easily preview AsyncImage with network calls in @Preview
            // but you can show a placeholder or a local image if needed.
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = "Placeholder Preview",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Mockup for permission UI in preview
            Text("Camera permission status will appear here.")
            Button(onClick = { /* Preview doesn't handle permission requests */ }) {
                Text("Request Camera Permission (Preview)")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionRequester(modifier: Modifier = Modifier) {
    // Remember the camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (cameraPermissionState.status) {
            is PermissionStatus.Granted -> {
                Text(stringResource(R.string.camera_permission_granted))
                // You can now show camera related content or navigate
            }
            is PermissionStatus.Denied -> {
                val status = cameraPermissionState.status as PermissionStatus.Denied
                if (status.shouldShowRationale) {
                    // If the user has denied the permission previously, show a rationale
                    Text(stringResource(R.string.camera_permission_rationale))
                }
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text(stringResource(R.string.request_camera_permission))
                }
                if (!status.shouldShowRationale) {
                    // If rationale is not needed (e.g., first time or "Don't ask again" selected)
                    // you might want to guide the user to settings if needed.
                    // For this example, we just show a generic denied message.
                     Text(stringResource(R.string.camera_permission_denied))
                }
            }
        }
    }
}
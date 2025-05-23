package com.ytone.longcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ytone.longcare.ui.theme.LongcareTheme // Assuming this theme exists or will be created

@dagger.hilt.android.AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LongcareTheme {
                // A simple Composable content
                Greeting("from com.ytone.longcare")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) { // Added Modifier for consistency
    Text(
        text = "Hello $name!",
        modifier = modifier // Apply the modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LongcareTheme {
        Greeting("from com.ytone.longcare")
    }
}
package com.ytone.longcare.features.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ytone.longcare.features.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val sampleData by viewModel.sampleData.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (sampleData.isEmpty()) {
            Button(onClick = { viewModel.loadSampleData() }) {
                Text("Load Sample Data")
            }
        } else {
            Text("Home Screen - Feature: home\nData: $sampleData")
        }
    }
}

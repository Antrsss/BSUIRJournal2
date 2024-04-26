package com.example.bsuirjournal2.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(
    selectedGroup: String,
    modifier: Modifier
) {
    Text(text = selectedGroup)
}
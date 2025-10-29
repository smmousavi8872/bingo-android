package com.smmousavi.developer.lvtgames.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.core.designsystem.components.StylousText

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        StylousText(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.Center),
            text = message,
        )
    }
}
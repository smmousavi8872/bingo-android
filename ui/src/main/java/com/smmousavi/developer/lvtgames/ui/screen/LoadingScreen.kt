package com.smmousavi.developer.lvtgames.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smmousavi.developer.lvtgames.core.designsystem.R
import com.smmousavi.developer.lvtgames.core.designsystem.components.StylousText


@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "Game Board Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        StylousText(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            text = "Loading Cards...",
            fontSize = 20.sp
        )
    }
}

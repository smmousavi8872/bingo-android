package com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class EdgeFadeSpec(
    val fadeRange: Dp = 64.dp,
    val fadeRatio: Float = 1f,
    val vertical: Boolean = true,
    val horizontal: Boolean = false,
)

val LocalEdgeFadeSpec = staticCompositionLocalOf { EdgeFadeSpec() }

@Composable
fun ProvideEdgeFadeSpec(spec: EdgeFadeSpec, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalEdgeFadeSpec provides spec, content = content)
}
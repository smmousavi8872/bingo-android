package com.smmousavi.developer.lvtgames.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

/**
 * A wrapper that fades its child at the container edges.
 *
 * The fade is applied via an alpha mask (DstIn) so it works with any content
 * (static or scrollable). The fade is only drawn when the child touches
 * the container edges in the corresponding axis (child >= container).
 *
 * @param fadeRange Thickness of the faded band at each edge.
 * @param fadeRatio Strength of the fade [0f, 1f], 1f = fully transparent at the edge.
 * @param fadeVertical If true, apply top & bottom fading.
 * @param fadeHorizontal If true, apply start & end fading.
 */
@Composable
fun EdgeFadeContainer(
    modifier: Modifier = Modifier,
    fadeRange: Dp = 24.dp,
    fadeRatio: Float = 1f,
    fadeVertical: Boolean = true,
    fadeHorizontal: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    var childSize by remember { mutableStateOf(IntSize.Zero) }

    // covert to [0f, 1f] alpha at the outermost edge,
    // 1f keeps content without fading, 0f removes it.
    val edgeAlpha = (1f - fadeRatio).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .onSizeChanged { parentSize = it }
            .graphicsLayer { alpha = 0.99f }
            .drawWithContent {
                // draw the content first.
                drawContent()

                val sizePx: Size = this.size

                // only fade on axes where child meets or exceeds the container
                val needsVertical =
                    fadeVertical && childSize.height >= parentSize.height && parentSize.height > 0
                val needsHorizontal =
                    fadeHorizontal && childSize.width >= parentSize.width && parentSize.width > 0

                if (!needsVertical && !needsHorizontal) return@drawWithContent

                // build a vertical mask: transparent at both ends, opaque in the middle.
                fun verticalMask(): Brush {
                    val r = (fadeRange.toPx() / sizePx.height).coerceIn(0f, 0.5f)
                    return Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.White.copy(alpha = edgeAlpha),
                            r to Color.White, // fully keep after the fade band
                            (1f - r) to Color.White, // fully keep until we approach the other edge
                            1f to Color.White.copy(alpha = edgeAlpha)
                        )
                    )
                }

                // build a horizontal mask: transparent at both ends, opaque in the middle.
                fun horizontalMask(): Brush {
                    val r = (fadeRange.toPx() / sizePx.width).coerceIn(0f, 0.5f)
                    return Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.White.copy(alpha = edgeAlpha),
                            r to Color.White,
                            (1f - r) to Color.White,
                            1f to Color.White.copy(alpha = edgeAlpha)
                        )
                    )
                }

                // apply masks via destination-in (alpha) blending.
                if (needsVertical) {
                    drawRect(
                        brush = verticalMask(),
                        blendMode = BlendMode.DstIn
                    )
                }
                if (needsHorizontal) {
                    drawRect(
                        brush = horizontalMask(),
                        blendMode = BlendMode.DstIn
                    )
                }
            }
    ) {
        // measure the child so we can decide if it touches the container edges.
        Box(
            modifier = Modifier.onSizeChanged { childSize = it },
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEdgeFadeContainer() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.DarkGray, Color.Black)
                )
            )
    ) {
        EdgeFadeContainer(
            fadeRange = 56.dp,
            fadeRatio = 1f,
            fadeVertical = true,
            fadeHorizontal = false,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(20) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(4.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF1E88E5),
                                        Color(0xFF90CAF9)
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Item #$index",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
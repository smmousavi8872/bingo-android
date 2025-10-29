package com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * A container that fades its content smoothly at the edges (top/bottom/left/right).
 *
 * The fade is applied using alpha blending, so it truly fades the content to transparent
 * — allowing background layers behind this container to show through.
 *
 * @param modifier The modifier to apply to the container.
 * @param contentPadding Padding applied to the inner content layout, *not* the fade mask.
 * @param topFadeStrength Strength of fade at the top (1f = full fade, 0f = no fade).
 * @param bottomFadeStrength Strength of fade at the bottom.
 * @param startFadeStrength Strength of fade at the start (left in LTR).
 * @param endFadeStrength Strength of fade at the end (right in LTR).
 * @param content The composable content inside the container.
 */
@Composable
fun EdgeFadeContainer(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    topFadeStrength: Float = 0f,
    bottomFadeStrength: Float = 0f,
    startFadeStrength: Float = 0f,
    endFadeStrength: Float = 0f,
    content: @Composable BoxScope.() -> Unit,
) {
    val spec = LocalEdgeFadeSpec.current
    val density = LocalDensity.current

    // Resolve padding to px once per recomposition
    val pad = remember(contentPadding, density) {
        with(density) {
            val start = contentPadding.calculateLeftPadding(LayoutDirection.Ltr).toPx()
            val end = contentPadding.calculateRightPadding(LayoutDirection.Ltr).toPx()
            val top = contentPadding.calculateTopPadding().toPx()
            val bot = contentPadding.calculateBottomPadding().toPx()
            floatArrayOf(start, top, end, bot)
        }
    }

    // Base transparency at edges from spec.fadeRatio
    fun edgeAlpha(strength: Float): Float {
        val s = strength.coerceIn(0f, 1f)
        val baseEdgeAlpha = (1f - spec.fadeRatio).coerceIn(0f, 1f) // 1 -> fully transparent
        // s=0 => alpha=1 (no fade). s=1 => alpha=baseEdgeAlpha (max fade)
        return (1f - s) + baseEdgeAlpha * s
    }

    Box(
        modifier = modifier
            // offscreen so DstIn creates real transparency over background
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()

                // Inner content area (after padding) where we apply fades
                val innerLeft = pad[0]
                val innerTop = pad[1]
                val innerRight = size.width - pad[2]
                val innerBottom = size.height - pad[3]
                val innerW = (innerRight - innerLeft).coerceAtLeast(0f)
                val innerH = (innerBottom - innerTop).coerceAtLeast(0f)
                if (innerW <= 0f || innerH <= 0f) return@drawWithContent

                val fadeRangePx = spec.fadeRange.toPx()
                val rV = (fadeRangePx / innerH).coerceIn(0f, 0.5f)
                val rH = (fadeRangePx / innerW).coerceIn(0f, 0.5f)

                // Vertical fade mask (top/bottom) inside padded bounds
                if (spec.vertical && (topFadeStrength > 0f || bottomFadeStrength > 0f)) {
                    val vBrush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.White.copy(alpha = edgeAlpha(topFadeStrength)),
                            rV to Color.White,
                            (1f - rV) to Color.White,
                            1f to Color.White.copy(alpha = edgeAlpha(bottomFadeStrength)),
                        ),
                        startY = innerTop,
                        endY = innerBottom
                    )
                    drawRect(
                        brush = vBrush,
                        topLeft = Offset(innerLeft, innerTop),
                        size = Size(innerW, innerH),
                        blendMode = BlendMode.DstIn
                    )
                }

                // Horizontal fade mask (start/end) inside padded bounds
                if (spec.horizontal && (startFadeStrength > 0f || endFadeStrength > 0f)) {
                    val hBrush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.White.copy(alpha = edgeAlpha(startFadeStrength)),
                            rH to Color.White,
                            (1f - rH) to Color.White,
                            1f to Color.White.copy(alpha = edgeAlpha(endFadeStrength)),
                        ),
                        startX = innerLeft,
                        endX = innerRight
                    )
                    drawRect(
                        brush = hBrush,
                        topLeft = Offset(innerLeft, innerTop),
                        size = Size(innerW, innerH),
                        blendMode = BlendMode.DstIn
                    )
                }
            }
    ) {
        // Just lay out your content with the same padding used for the fade bounds
        Box(Modifier.padding(contentPadding), content = content)
    }
}

@Preview(showBackground = true)
@Composable
fun FadedListPreview() {
    val listState = rememberLazyListState()
    val fadeState = rememberEdgeFadeState(listState)

    ProvideEdgeFadeSpec(
        EdgeFadeSpec(fadeRange = 144.dp, fadeRatio = 1f, vertical = true)
    ) {
        EdgeFadeContainer(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
            topFadeStrength = fadeState.top.value,
            bottomFadeStrength = fadeState.bottom.value,
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(20) { i ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF2E7D32), Color(0xFF81C784))
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Item #$i", color = Color.White)
                    }
                }
            }
        }
    }
}
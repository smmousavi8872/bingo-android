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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
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
    topFadeStrength: Float = 1f,
    bottomFadeStrength: Float = 1f,
    startFadeStrength: Float = 1f,
    endFadeStrength: Float = 1f,
    content: @Composable BoxScope.() -> Unit,
) {
    val spec = LocalEdgeFadeSpec.current
    val density = LocalDensity.current

    // Measure parent & inner child (post-padding) sizes
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    var childSize by remember { mutableStateOf(IntSize.Zero) }

    // Convert padding to px (needed for correct edge detection)
    val padStartPx =
        with(density) { contentPadding.calculateLeftPadding(LayoutDirection.Ltr).toPx() }
    val padEndPx =
        with(density) { contentPadding.calculateRightPadding(LayoutDirection.Ltr).toPx() }
    val padTopPx = with(density) { contentPadding.calculateTopPadding().toPx() }
    val padBotPx = with(density) { contentPadding.calculateBottomPadding().toPx() }

    // Base transparency at the outermost edge:
    // fadeRatio=1f -> fully transparent at edges; 0f -> no fade at edges
    val baseEdgeAlpha = (1f - spec.fadeRatio).coerceIn(0f, 1f)

    // Strength (0..1) → alpha at edge (1..baseEdgeAlpha)
    fun edgeAlphaFor(strength: Float): Float {
        val s = strength.coerceIn(0f, 1f)
        // s=0 → alpha=1 (no fade); s=1 → alpha=baseEdgeAlpha (max fade)
        return (1f - s) + baseEdgeAlpha * s
    }

    Box(
        modifier = modifier
            .onSizeChanged { parentSize = it }
            .graphicsLayer {
                // needed so DstIn actually punches true transparency over background
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithContent {
                drawContent()

                if (parentSize.width == 0 || parentSize.height == 0) return@drawWithContent

                // compare inner content + padding against parent
                val contentTotalHeight = childSize.height + padTopPx + padBotPx
                val contentTotalWidth = childSize.width + padStartPx + padEndPx

                val needsV = spec.vertical && contentTotalHeight >= parentSize.height
                val needsH = spec.horizontal && contentTotalWidth >= parentSize.width

                if (!needsV && !needsH) return@drawWithContent

                val sz: Size = this.size
                val rV = (spec.fadeRange.toPx() / sz.height).coerceIn(0f, 0.5f)
                val rH = (spec.fadeRange.toPx() / sz.width).coerceIn(0f, 0.5f)

                // Vertical mask (top & bottom)
                if (needsV) {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color.White.copy(alpha = edgeAlphaFor(topFadeStrength)),
                                rV to Color.White,
                                (1f - rV) to Color.White,
                                1f to Color.White.copy(alpha = edgeAlphaFor(bottomFadeStrength)),
                            )
                        ),
                        size = sz,
                        blendMode = BlendMode.DstIn
                    )
                }

                // Horizontal mask (start & end)
                if (needsH) {
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colorStops = arrayOf(
                                0f to Color.White.copy(alpha = edgeAlphaFor(startFadeStrength)),
                                rH to Color.White,
                                (1f - rH) to Color.White,
                                1f to Color.White.copy(alpha = edgeAlphaFor(endFadeStrength)),
                            )
                        ),
                        size = sz,
                        blendMode = BlendMode.DstIn
                    )
                }
            }
    ) {
        // still apply padding to layout, childSize measures inner content (post-padding)
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .onSizeChanged { childSize = it },
            content = content
        )
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
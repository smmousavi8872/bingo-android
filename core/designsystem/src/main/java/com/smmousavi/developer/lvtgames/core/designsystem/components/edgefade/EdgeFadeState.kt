package com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity

@Stable
class EdgeFadeState internal constructor(
    val top: State<Float>,
    val bottom: State<Float>,
    val start: State<Float>,
    val end: State<Float>,
)

/**
 * Unified edge-fade state for both vertical and horizontal scrolling lists.
 * Respects the flags in [EdgeFadeSpec] (vertical/horizontal) and returns
 * animated strengths for each edge in [0f..1f].
 *
 * - top/bottom increase as you move away from the first/last rows
 * - start/end  increase as you move away from the first/last columns
 */
@Composable
fun rememberEdgeFadeState(
    listState: LazyListState,
): EdgeFadeState {
    val spec = LocalEdgeFadeSpec.current
    val density = LocalDensity.current
    val fadeRangePx = with(density) { spec.fadeRange.toPx() }

    // Vertical fade alignments
    val rawTop = remember {
        derivedStateOf {
            if (!spec.vertical) 0f
            else {
                val atTop = listState.firstVisibleItemIndex == 0
                val offset = listState.firstVisibleItemScrollOffset.toFloat()
                if (!atTop) 1f else (offset / fadeRangePx).coerceIn(0f, 1f)
            }
        }
    }
    val animTop by animateFloatAsState(rawTop.value, label = "EdgeFadeTop")

    val rawBottom = remember {
        derivedStateOf {
            if (!spec.vertical) 0f
            else {
                val info = listState.layoutInfo
                if (info.totalItemsCount == 0) 0f else {
                    val lastVis = info.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf 0f
                    val isLast = lastVis.index == info.totalItemsCount - 1
                    if (!isLast) 1f else {
                        // viewport bottom position
                        val viewportBottom = info.viewportEndOffset - info.afterContentPadding
                        val lastItemBottom = lastVis.offset + lastVis.size
                        val distanceFromEnd = (viewportBottom - lastItemBottom).toFloat()

                        // smoother fade reentry when scrolling up
                        val fadeStart = -fadeRangePx
                        val fadeEnd = 0f
                        val normalized = ((distanceFromEnd - fadeStart) / (fadeEnd - fadeStart))
                            .coerceIn(0f, 1f)
                        1f - normalized // 0 when fully bottomed, 1 when leaving
                    }
                }
            }
        }
    }
    val animBottom by animateFloatAsState(rawBottom.value, label = "EdgeFadeBottom")

    // Horizontal fade alignments
    val rawStart = remember {
        derivedStateOf {
            if (!spec.horizontal) 0f
            else {
                val atStart = listState.firstVisibleItemIndex == 0
                val offset = listState.firstVisibleItemScrollOffset.toFloat()
                if (!atStart) 1f else (offset / fadeRangePx).coerceIn(0f, 1f)
            }
        }
    }
    val animStart by animateFloatAsState(rawStart.value, label = "EdgeFadeStart")

    val rawEnd = remember {
        derivedStateOf {
            if (!spec.horizontal) 0f
            else {
                val info = listState.layoutInfo
                if (info.totalItemsCount == 0) 0f else {
                    val lastVis = info.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf 0f
                    val isLast = lastVis.index == info.totalItemsCount - 1
                    if (!isLast) 1f else {
                        val viewportEnd = info.viewportEndOffset - info.afterContentPadding
                        val lastItemEnd = lastVis.offset + lastVis.size
                        val distanceFromEnd = (viewportEnd - lastItemEnd).toFloat()

                        val fadeStart = -fadeRangePx
                        val fadeEnd = 0f
                        val normalized = ((distanceFromEnd - fadeStart) / (fadeEnd - fadeStart))
                            .coerceIn(0f, 1f)
                        1f - normalized
                    }
                }
            }
        }
    }
    val animEnd by animateFloatAsState(rawEnd.value, label = "EdgeFadeEnd")

    // Stable wrappers for Compose optimization
    val topState = remember { derivedStateOf { animTop } }
    val bottomState = remember { derivedStateOf { animBottom } }
    val startState = remember { derivedStateOf { animStart } }
    val endState = remember { derivedStateOf { animEnd } }

    return remember { EdgeFadeState(topState, bottomState, startState, endState) }
}
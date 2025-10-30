package com.smmousavi.developer.lvtgames.core.designsystem.components.ring

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp


/**
 * Describes a ring (stroke) drawn as a circle inside the cell.
 *
 * @param color  ring color
 * @param width  stroke width
 * @param radiusRatio circle radius as a fraction of the cell’s min(width, height).
 *                    Typical values: outer ≈ 0.36–0.40, inner ≈ 0.28–0.32
 */
data class RingSpec(
    val color: Color,
    val width: Dp,
    val radiusRatio: Float
)
package com.smmousavi.developer.lvtgames.core.designsystem.components.ring

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * A reusable, square cell that can draw an optional filled circle behind its content,
 * plus up to two concentric stroke rings on top. You pass any Composable as [content].
 *
 * Draw order:
 * - (optional) filledCircleColor circle (behind content)
 * - content()
 * - (optional) rings (over content)
 *
 * @param size               square size of the cell.
 * @param backgroundColor    background of the cell (rectangle).
 * @param borderColor        optional thin rect border around the cell (0.dp to disable).
 * @param filledCircleColor  optional filled circle behind content (null = disabled).
 * @param outerRing          optional outer ring spec (null = disabled).
 * @param innerRing          optional inner ring spec (null = disabled).
 * @param onClick            optional click callback; null = not clickable.
 * @param content            slot content (e.g., Text with the number).
 */
@Composable
fun RingButton(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    backgroundColor: Color,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.5.dp,
    filledCircleColor: Color? = null,
    outerRing: RingSpec? = null,
    innerRing: RingSpec? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val clickable = remember(onClick) {
        if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    }

    Surface(
        modifier = modifier
            .size(size)
            .then(clickable),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor)
                .let {
                    if (borderWidth > 0.dp && borderColor != Color.Transparent) {
                        it.border(borderWidth, borderColor)
                    } else it
                }
                .drawWithContent {
                    val w = this.size.width
                    val h = this.size.height
                    val d = minOf(w, h)
                    val cx = w / 2f
                    val cy = h / 2f

                    // filled circle behind content
                    filledCircleColor?.let { fill ->
                        // pick a sensible default radius if both rings are null
                        val defaultRatio = 0.38f
                        val rr = (outerRing?.radiusRatio ?: innerRing?.radiusRatio ?: defaultRatio)
                            .coerceIn(0.05f, 0.48f)
                        drawCircle(
                            color = fill,
                            radius = d * rr,
                            center = Offset(cx, cy)
                        )
                    }

                    // content in the middle
                    drawContent()

                    // rings on top of content
                    outerRing?.let { ring ->
                        drawCircle(
                            color = ring.color,
                            radius = d * ring.radiusRatio.coerceIn(0.05f, 0.48f),
                            center = Offset(cx, cy),
                            style = Stroke(width = ring.width.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    innerRing?.let { ring ->
                        drawCircle(
                            color = ring.color,
                            radius = d * ring.radiusRatio.coerceIn(0.05f, 0.48f),
                            center = Offset(cx, cy),
                            style = Stroke(width = ring.width.toPx(), cap = StrokeCap.Round)
                        )
                    }
                },
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RingButtonPreview() {
    RingButton(
        size = 80.dp,
        backgroundColor = Color(0xFFFFFFFF),
        borderColor = Color(0xFF424242),
        borderWidth = 1.dp,
        filledCircleColor = Color(0xFF4CAF50),
        outerRing = RingSpec(
            color = Color(0xFF2E7D32),
            width = 5.dp,
            radiusRatio = 0.38f
        ),
        innerRing = RingSpec(
            color = Color(0xFF81C784),
            width = 4.dp,
            radiusRatio = 0.30f
        ),
        onClick = {}
    ) {
        Text(
            text = "WIN",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
    }
}

package com.smmousavi.developer.lvtgames.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max


/**
 * A customizable composable text renderer that supports both
 * **stroke outlines** and **shadow effects** for visually rich titles
 * or banner text.
 *
 * This component is useful for implementing stylized game titles,
 * logos, and decorative labels.
 *
 * ### Features
 * - Draws a **crisp outer stroke** (outline) around the text.
 * - Draws the **fill text** with optional drop shadow.
 * - Uses Android’s low-level [android.graphics.Paint] for pixel-perfect
 *   control over stroke width, shadow, and fill rendering.
 * - Automatically pads the drawing area to prevent stroke or shadow clipping.
 *
 * ### Parameters
 * @param text The text string to render.
 * @param color The fill (inner) text color.
 * @param fontSize The size of the text. Default is 48sp.
 * @param fontWeight The weight of the typeface (e.g., [FontWeight.Bold]).
 *
 * @param strokeWidth The width of the outer stroke (outline). Set to `0.dp` to disable.
 * @param strokeColor The color of the stroke outline.
 *
 * @param shadowColor The color of the shadow cast behind the text fill.
 * @param shadowOffsetX The horizontal shadow offset in [Dp].
 * @param shadowOffsetY The vertical shadow offset in [Dp].
 * @param shadowBlur The blur radius of the shadow. Set to `0.dp` to disable.
 *
 * @param letterSpacingEm Letter spacing in "em" units (Android [androidx.compose.ui.graphics.Paint.letterSpacing]).
 *                        Set to `0f` to disable.
 *
 * ### Example
 * ```
 * StylousText(
 *     text = "Your Text",
 *     color = Color(0xFFFFD700),
 *     fontSize = 52.sp,
 *     fontWeight = FontWeight.Bold,
 *     strokeWidth = 6.dp,
 *     strokeColor = Color.Black,
 *     shadowColor = Color(0xFFB71C1C).copy(alpha = 0.55f),
 *     shadowOffsetX = 2.dp,
 *     shadowOffsetY = 2.dp,
 *     shadowBlur = 10.dp,
 *     letterSpacingEm = 0.02f
 * )
 * ```
 *
 */
@Composable
fun StylousText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color(0xFFFFD700),
    fontSize: TextUnit = 24.sp,
    fontWeight: FontWeight? = FontWeight.Bold,
    strokeWidth: Dp = 2.dp,
    strokeColor: Color = Color.Black,
    shadowColor: Color = Color(0xFFB71C1C).copy(alpha = 0.7f),
    shadowOffsetX: Dp = 1.dp,
    shadowOffsetY: Dp = 1.dp,
    shadowBlur: Dp = 4.dp,
    letterSpacingEm: Float = 0f,
) {
    val density = LocalDensity.current
    val textSizePx = with(density) { fontSize.toPx() }

    // Baseline at 24sp, scale everything from that
    val baselinePx = with(density) { 24.sp.toPx() }
    val scale = (textSizePx / baselinePx).coerceIn(0.5f, 6f)

    // Ensure sensible minimums relative to font size (but honor explicit values if larger)
    val minStrokePx     = textSizePx * 0.05f  // about 5% of glyph height
    val minShadowBlurPx = textSizePx * 0.08f
    val minShadowOffsPx = textSizePx * 0.02f

    val strokePx = max(
        with(density) { strokeWidth.toPx() },
        minStrokePx
    )
    val shadowDx = max(with(density) { shadowOffsetX.toPx() }, minShadowOffsPx)
    val shadowDy = max(with(density) { shadowOffsetY.toPx() }, minShadowOffsPx)
    val shadowBlurPx = max(with(density) { shadowBlur.toPx() }, minShadowBlurPx)

    val paint = remember { android.graphics.Paint().apply { isAntiAlias = true } }
    remember(text, textSizePx, fontWeight, letterSpacingEm) {
        paint.apply {
            reset()
            isAntiAlias = true
            textSize = textSizePx
            typeface = android.graphics.Typeface.create(
                android.graphics.Typeface.DEFAULT,
                if ((fontWeight?.weight ?: 400) >= 700) android.graphics.Typeface.BOLD
                else android.graphics.Typeface.NORMAL
            )
            letterSpacing = letterSpacingEm
        }
    }

    val measured = remember(text, textSizePx, letterSpacingEm, fontWeight) {
        val w = paint.measureText(text)
        val fm = paint.fontMetrics
        val h = fm.descent - fm.ascent
        TextMeasure(width = w, height = h, baseline = -fm.ascent)
    }

    // extra padding so stroke & shadow aren’t clipped (scaled)
    val extraPx = max(strokePx * 1.2f, shadowBlurPx + max(shadowDx, shadowDy))
    val layoutWidthPx = measured.width + extraPx * 2
    val layoutHeightPx = measured.height + extraPx * 2

    Layout(
        content = {
            Canvas(Modifier) {
                val x = extraPx
                val y = extraPx + measured.baseline

                // Stroke (outline)
                if (strokePx > 0f) {
                    paint.apply {
                        style = android.graphics.Paint.Style.STROKE
                        this.strokeWidth = strokePx
                        this.color = strokeColor.toArgb()
                        clearShadowLayer()
                    }
                    drawContext.canvas.nativeCanvas.drawText(text, x, y, paint)
                }

                // Fill + shadow
                paint.apply {
                    style = android.graphics.Paint.Style.FILL
                    this.color = color.toArgb()
                    if (shadowBlurPx > 0f || shadowDx != 0f || shadowDy != 0f) {
                        setShadowLayer(shadowBlurPx, shadowDx, shadowDy, shadowColor.toArgb())
                    } else {
                        clearShadowLayer()
                    }
                }
                drawContext.canvas.nativeCanvas.drawText(text, x, y, paint)
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val placeable = measurables.first().measure(constraints.copy(minWidth = 0, minHeight = 0))
        val w = layoutWidthPx.toInt().coerceIn(0, constraints.maxWidth)
        val h = layoutHeightPx.toInt().coerceIn(0, constraints.maxHeight)
        layout(w, h) { placeable.place(0, 0) }
    }
}

private data class TextMeasure(
    val width: Float,
    val height: Float,
    val baseline: Float,
)

@Preview(showBackground = true)
@Composable
fun StylousTextPreview() {
    Box {
        StylousText(
            text = "Your Text",
            color = Color(0xFFFFD700),
            fontSize = 52.sp,
            fontWeight = FontWeight.Bold,
            strokeWidth = 6.dp,
            strokeColor = Color.Black,
            shadowColor = Color(0xFFB71C1C).copy(alpha = 0.55f),
            shadowOffsetX = 2.dp,
            shadowOffsetY = 2.dp,
            shadowBlur = 10.dp,
            letterSpacingEm = 0.02f
        )
    }
}
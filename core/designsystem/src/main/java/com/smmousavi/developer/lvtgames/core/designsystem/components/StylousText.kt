package com.smmousavi.developer.lvtgames.core.designsystem.components

import android.graphics.Paint as FwPaint
import android.graphics.Typeface
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
 * logos, and decorative labels (e.g., the “BINGO GHANA” logo style).
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
    color: Color = Color.White,
    fontSize: TextUnit = 48.sp,
    fontWeight: FontWeight? = null,
    strokeWidth: Dp = 0.dp,
    strokeColor: Color = Color.Black,
    shadowColor: Color = Color.Black.copy(alpha = 0.7f),
    shadowOffsetX: Dp = 2.dp,
    shadowOffsetY: Dp = 2.dp,
    shadowBlur: Dp = 6.dp,
    letterSpacingEm: Float = 0f,
) {
    val density = LocalDensity.current
    val textSizePx = with(density) { fontSize.toPx() }
    val strokePx = with(density) { strokeWidth.toPx() }
    val shadowDx = with(density) { shadowOffsetX.toPx() }
    val shadowDy = with(density) { shadowOffsetY.toPx() }
    val shadowBlurPx = with(density) { shadowBlur.toPx() }

    // set a single Android Paint to mutate, not reassigning
    val paint = remember { FwPaint().apply { isAntiAlias = true } }
    // configure base text attributes used for measurement
    remember(text, textSizePx, fontWeight, letterSpacingEm) {
        paint.apply {
            reset()
            this.isAntiAlias = true
            this.textSize = textSizePx
            this.typeface = Typeface.create(
                Typeface.DEFAULT,
                if ((fontWeight?.weight ?: 400) >= 700) Typeface.BOLD else Typeface.NORMAL
            )
            this.letterSpacing = letterSpacingEm
        }
    }
    // measure single line
    val measured = remember(text, textSizePx, letterSpacingEm, fontWeight) {
        val width = paint.measureText(text)
        val fm = paint.fontMetrics
        val height = fm.descent - fm.ascent
        TextMeasure(width = width, height = height, baseline = -fm.ascent)
    }
    // extra padding so stroke and shadow don’t get clipped
    val extraPx = max(
        strokePx * 1.2f,
        shadowBlurPx + max(shadowDx, shadowDy)
    )
    val layoutWidthPx = measured.width + extraPx * 2
    val layoutHeightPx = measured.height + extraPx * 2

    Layout(
        content = {
            Canvas(Modifier) {
                // draw at (x, y) with baseline aligned inside our padded canvas
                val x = extraPx
                val y = extraPx + measured.baseline

                // ----- STROKE (optional) -----
                if (strokePx > 0f) {
                    paint.apply {
                        this.style = FwPaint.Style.STROKE
                        this.strokeWidth = strokePx
                        this.color = strokeColor.toArgb()
                        clearShadowLayer() // keep stroke crisp
                    }
                    drawContext.canvas.nativeCanvas.drawText(text, x, y, paint)
                }

                // ----- FILL (with optional shadow) -----
                paint.apply {
                    this.style = FwPaint.Style.FILL
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
        layout(w, h) {
            placeable.place(0, 0)
        }
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
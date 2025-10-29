package com.smmousavi.developer.lvtgames.feature.cards.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun rememberCardScaleState(
    rows: Int,
    cols: Int,
    maxW: Dp,
    maxH: Dp,
    headerEstimateDp: Dp = 56.dp,   // space for top row
    minCell: Dp = 24.dp,            // safety for very small screens
    maxCell: Dp = 80.dp,            // safety for very large screens
): CardScaleState {
    // compute the largest cell that fits both width and height
    val gridCellByW = (maxW / cols).coerceAtLeast(1.dp)
    val gridCellByH = ((maxH - headerEstimateDp).coerceAtLeast(1.dp) / rows).coerceAtLeast(1.dp)
    val cell = minOf(gridCellByW, gridCellByH).coerceIn(minCell, maxCell)

    // derive other sizes from cell
    val pad = (cell * 0.25f).coerceIn(6.dp, 16.dp)
    val innerPad = (cell * 0.2f).coerceIn(4.dp, 12.dp)
    val outerRadius = (cell * 0.2f).coerceIn(6.dp, 14.dp)
    val innerRadius = (cell * 0.12f).coerceIn(4.dp, 10.dp)
    val border = (cell * 0.03f).coerceIn(1.dp, 2.dp)
    val subBorder = (cell * 0.02f).coerceIn(1.dp, 1.5.dp)

    // font sizes scale with cell size
    // ~11sp at cell≈40dp, grows/shrinks proportionally
    val titleSp = (cell.value * 0.28f).sp
    val labelSp = (cell.value * 0.28f).sp
    val valueSp = (cell.value * 0.28f).sp

    return CardScaleState(
        cell = cell,
        pad = pad,
        innerPad = innerPad,
        outerRadius = outerRadius,
        innerRadius = innerRadius,
        border = border,
        subBorder = subBorder,
        titleSp = titleSp,
        labelSp = labelSp,
        valueSp = valueSp,
    )
}

@Stable
data class CardScaleState(
    val cell: Dp,
    val pad: Dp,
    val innerPad: Dp,
    val outerRadius: Dp,
    val innerRadius: Dp,
    val border: Dp,
    val subBorder: Dp,
    val titleSp: TextUnit,
    val labelSp: TextUnit,
    val valueSp: TextUnit,
)
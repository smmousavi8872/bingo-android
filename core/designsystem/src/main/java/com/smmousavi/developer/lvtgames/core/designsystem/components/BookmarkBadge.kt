package com.smmousavi.developer.lvtgames.core.designsystem.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.core.designsystem.R

/**
 * A reusable badge-style component that displays a rounded square with a
 * gradient fill, golden border, and a centered bookmark icon.
 *
 * It resembles the bookmark badge shown at the top-right of the Bingo card.
 *
 * @param modifier optional [Modifier] for layout and styling.
 * @param colors color palette provided by server or theme.
 * @param size total badge size (default: 40.dp).
 * @param cornerRadius rounding for badge corners.
 * @param onClick optional click listener; if null, badge is non-interactive.
 */
@Composable
fun BookmarkBadge(
    modifier: Modifier = Modifier,
    colors: BookmarkBadgeColors = BookmarkBadgeColors(),
    size: Dp = 40.dp,
    cornerRadius: Dp = 4.dp,
    onClick: (() -> Unit)? = null,
) {
    // Scale factor vs a 40.dp design baseline
    val k = (size.value / 40f).coerceAtLeast(0.5f)

    val shape = RoundedCornerShape(
        if (cornerRadius == 4.dp) (size * 0.1f) else cornerRadius
    )
    val interaction = remember { MutableInteractionSource() }

    val outerBorder = (1f * k).dp
    val innerBorder = (1f * k).dp
    val innerPad = (1f * k).dp
    val elevation = (6f * k).dp

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                clip = false
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interaction,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .size(size)
            .background(
                brush = Brush.linearGradient(
                    listOf(colors.fillStart, colors.fillMid, colors.fillEnd)
                ),
                shape = shape
            )
            .border(width = outerBorder, color = colors.frame, shape = shape)
            .padding(innerPad)
            .border(width = innerBorder, color = colors.fillEnd, shape = shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_bookmark_white_fill),
            contentDescription = "Bookmark",
            tint = colors.icon,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

/** Defines the server-driven color palette for [BookmarkBadge]. */
data class BookmarkBadgeColors(
    val frame: Color = Color(0xFFF3B33E),
    val fillStart: Color = Color(0xFF8A3FFF),
    val fillMid: Color = Color(0xFF7A2DF8),
    val fillEnd: Color = Color(0xFF530FD2),
    val icon: Color = Color.White,
)

@Preview(showBackground = true)
@Composable
fun BookmarkBadgePreview() {
    BookmarkBadge()
}
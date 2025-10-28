package com.smmousavi.developer.lvtgames.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.smmousavi.developer.lvtgames.core.designsystem.R

val SuperFrosting = FontFamily(
    Font(R.font.super_frosting_r9z4o, FontWeight.Normal),
)

val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = SuperFrosting,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = SuperFrosting,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),

    titleSmall = TextStyle(
        fontFamily = SuperFrosting,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = SuperFrosting,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = SuperFrosting,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    bodySmall = TextStyle(
        fontFamily = SuperFrosting,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    )
)
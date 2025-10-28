package com.smmousavi.developer.lvtgames.feature.cards.uimodel

import androidx.compose.ui.graphics.Color

data class CardsUiModel(
    val cards: List<CardUiModel>,
)

data class CardUiModel(
    val id: Int,
    val name: String,
    val matrix: List<List<Int?>>,
    val prizes: List<PrizeUiModel>,
    val bet: Int,
    val pieceColors: PieceColors,
    val background: Color,
    val backgroundGradient1: Color?,
    val backgroundGradient2: Color?,
    val backgroundGradient3: Color?,
    val titleColor: Color,
    val textColor: Color,
    val borderColor: Color,
)

data class PrizeUiModel(
    val id: Int,
    val title: String,
    val amount: Int,
    val type: String?,
    val number: Int?,
)

data class PieceColors(
    val primaryCell: Color,
    val secondaryCell: Color,
    val textOnPrimary: Color,
    val textOnSecondary: Color,
    val tokenOuterRing: Color,
    val tokenInnerRing: Color,
    val tokenInnerFill: Color,
    val highlightOverlay: Color? = null,
    val selectedOverlay: Color? = null,
) {
    companion object {
        val DEFAULT = PieceColors(
            primaryCell = Color(0xff54c62f),
            secondaryCell = Color(0xffffffff),
            textOnPrimary = Color(0xff3d7b31),
            textOnSecondary = Color(0xff3d7b31),
            tokenOuterRing = Color(0xff3d7b31),
            tokenInnerRing = Color(0xff54c62f),
            tokenInnerFill = Color(0xffecfed6),
            highlightOverlay = null,
            selectedOverlay = null,
        )
    }
}
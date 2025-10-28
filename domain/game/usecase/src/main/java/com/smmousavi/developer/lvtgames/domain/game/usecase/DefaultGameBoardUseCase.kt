package com.smmousavi.developer.lvtgames.domain.game.usecase

import com.smmousavi.developer.lvtgames.core.model.domain.game.FourCornersPattern
import com.smmousavi.developer.lvtgames.core.model.domain.game.FullHousePattern
import com.smmousavi.developer.lvtgames.core.model.domain.game.RowsPattern
import com.smmousavi.developer.lvtgames.core.model.domain.game.WinPattern

class DefaultGameBoardUseCase : GameBoardUseCase {
    override val patterns: List<WinPattern>
        get() = listOf(RowsPattern, FourCornersPattern, FullHousePattern)
}
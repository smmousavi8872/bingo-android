package com.smmousavi.developer.lvtgames.domain.game.usecase

import com.smmousavi.developer.lvtgames.core.model.domain.game.FourCornersPattern
import com.smmousavi.developer.lvtgames.core.model.domain.game.FullHousePattern
import com.smmousavi.developer.lvtgames.core.model.domain.game.RowsPattern
import com.smmousavi.developer.lvtgames.core.model.domain.game.WinPattern

class DefaultBoardUseCase : BoardUseCase {
    override val patterns: List<WinPattern>
        get() = listOf(RowsPattern, FourCornersPattern, FullHousePattern)
}
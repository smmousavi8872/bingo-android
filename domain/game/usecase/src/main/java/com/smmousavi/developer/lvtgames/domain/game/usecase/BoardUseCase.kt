package com.smmousavi.developer.lvtgames.domain.game.usecase

import com.smmousavi.developer.lvtgames.core.model.domain.game.WinPattern

interface BoardUseCase {
    val patterns: List<WinPattern>
}
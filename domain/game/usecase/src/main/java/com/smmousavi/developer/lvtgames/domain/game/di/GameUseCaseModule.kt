package com.smmousavi.developer.lvtgames.domain.game.di

import com.smmousavi.developer.lvtgames.domain.game.usecase.DefaultGameBoardUseCase
import com.smmousavi.developer.lvtgames.domain.game.usecase.GameBoardUseCase
import org.koin.dsl.module

val gameUseCaseModule = module {
    factory<GameBoardUseCase> { DefaultGameBoardUseCase() }
}
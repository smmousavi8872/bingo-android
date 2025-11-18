package com.smmousavi.developer.lvtgames.domain.game.di

import com.smmousavi.developer.lvtgames.domain.game.usecase.DefaultBoardUseCase
import com.smmousavi.developer.lvtgames.domain.game.usecase.BoardUseCase
import org.koin.dsl.module

val gameUseCaseModule = module {
    factory<BoardUseCase> { DefaultBoardUseCase() }
}
package com.smmousavi.developer.lvtgames.feature.game.di

import com.smmousavi.developer.lvtgames.feature.game.GameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gameViewModelModule = module {
    viewModel<GameViewModel> { GameViewModel(get()) }
}
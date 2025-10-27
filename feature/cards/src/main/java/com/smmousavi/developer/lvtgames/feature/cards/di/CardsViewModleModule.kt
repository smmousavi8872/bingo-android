package com.smmousavi.developer.lvtgames.feature.cards.di

import com.smmousavi.developer.lvtgames.feature.cards.CardsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cardsViewModelModule = module {
    viewModel<CardsViewModel> { CardsViewModel(get()) }
}
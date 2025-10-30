package com.smmousavi.developer.lvtgames.ui.screen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smmousavi.developer.lvtgames.core.designsystem.UiState
import com.smmousavi.developer.lvtgames.feature.cards.CardsViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(viewModel: CardsViewModel = koinViewModel()) {

    val state = viewModel.state.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    LaunchedEffect(Unit) {
        delay(1000) // just to make sure is displayed
        viewModel.observeCards()
    }

    Surface {
        when (val cardsState = state.value) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Error -> ErrorScreen(cardsState.message)
            is UiState.Success -> GameScreen(
                cards = cardsState.data.cards,
            )
        }
    }
}
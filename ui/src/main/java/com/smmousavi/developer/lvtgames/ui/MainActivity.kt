package com.smmousavi.developer.lvtgames.ui


import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smmousavi.developer.lvtgames.core.designsystem.UiState
import com.smmousavi.developer.lvtgames.feature.cards.CardsViewModel
import com.smmousavi.developer.lvtgames.ui.screen.ErrorScreen
import com.smmousavi.developer.lvtgames.ui.screen.GameScreen
import com.smmousavi.developer.lvtgames.ui.screen.LoadingScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        enableEdgeToEdge()
        setContent {
            BingoApp()
        }
    }
}

@Composable
fun BingoApp(
    viewModel: CardsViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    LaunchedEffect(Unit) {
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

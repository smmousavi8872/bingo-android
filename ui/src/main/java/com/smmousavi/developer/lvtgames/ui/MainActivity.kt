package com.smmousavi.developer.lvtgames.ui


import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smmousavi.developer.lvtgames.core.designsystem.UiState
import com.smmousavi.developer.lvtgames.core.designsystem.components.StylousText
import com.smmousavi.developer.lvtgames.feature.cards.CardsViewModel
import com.smmousavi.developer.lvtgames.ui.screen.GameScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    Surface(color = MaterialTheme.colorScheme.background) {
        when (val cardsState = state.value) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Error -> ErrorScreen(cardsState.message)
            is UiState.Success -> GameScreen(cards = cardsState.data.cards)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        StylousText(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.Center),
            text = "Loading Cards...",
        )
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        StylousText(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.Center),
            text = message,
        )
    }
}
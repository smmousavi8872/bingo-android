package com.smmousavi.developer.lvtgames.ui


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smmousavi.developer.lvtgames.core.designsystem.UiState
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardsUiModel
import com.smmousavi.developer.lvtgames.feature.cards.CardsViewModel
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
        when (val ui = state.value) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Error -> ErrorScreen(ui.message)
            is UiState.Success -> GameScreen(ui.data)
        }
    }
}

@Composable
fun LoadingScreen() {
    Text("Loading cards...")
}

@Composable
fun ErrorScreen(message: String?) {
    Text("Error: ${message ?: "Unknown"}")
}

@Composable
fun GameScreen(uiModel: CardsUiModel) {
    // For now, just print out the card names
    Text(
        fontSize = 32.sp,
        text = "Received ${uiModel.cards.size} cards from server.",
        modifier = Modifier.padding(32.dp)
    )
}
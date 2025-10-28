package com.smmousavi.developer.lvtgames.feature.game.uimodel

import com.smmousavi.developer.lvtgames.core.model.domain.game.BINGO_MAX_NUMBER
import com.smmousavi.developer.lvtgames.core.model.domain.game.BINGO_MIN_NUMBER
import com.smmousavi.developer.lvtgames.core.model.domain.game.GameBoard
import com.smmousavi.developer.lvtgames.core.model.domain.game.WinCheck
import com.smmousavi.developer.lvtgames.core.model.domain.game.WinPattern
import com.smmousavi.developer.lvtgames.core.model.domain.game.checkWins


sealed interface DrawMode {
    data object Manual : DrawMode
    data class Auto(val intervalMillis: Long = 1200L) : DrawMode
}

data class GameUiModel(
    val gameBoard: GameBoard? = null,
    val drawn: Set<Int> = emptySet(),
    val lastDrawn: Int? = null,
    val mode: DrawMode = DrawMode.Manual,
    val isRunning: Boolean = false,
    val wins: WinCheck = WinCheck(emptyMap()),
    val errorMessage: String? = null,
)

sealed interface GameAction {
    data class Start(val gameBoard: GameBoard, val mode: DrawMode) : GameAction
    data object Pause : GameAction
    data object Resume : GameAction
    data object Reset : GameAction
    data class DrawOne(val number: Int) : GameAction
    data class Fail(val message: String) : GameAction
}

fun reduce(state: GameUiModel, action: GameAction, patterns: List<WinPattern>): GameUiModel =
    when (action) {
        is GameAction.Start -> {
            val wins = checkWins(action.gameBoard, emptySet(), patterns)
            state.copy(
                gameBoard = action.gameBoard,
                drawn = emptySet(),
                lastDrawn = null,
                mode = action.mode,
                isRunning = action.mode is DrawMode.Auto,
                wins = wins,
                errorMessage = null
            )
        }

        GameAction.Pause -> state.copy(isRunning = false)
        GameAction.Resume -> state.copy(isRunning = state.mode is DrawMode.Auto)
        GameAction.Reset -> {
            val b = state.gameBoard
            state.copy(
                drawn = emptySet(),
                lastDrawn = null,
                isRunning = false,
                wins = if (b != null) checkWins(b, emptySet(), patterns) else WinCheck(emptyMap()),
                errorMessage = null
            )
        }

        is GameAction.DrawOne -> {
            val b = state.gameBoard ?: return state
            val n = action.number.coerceIn(BINGO_MIN_NUMBER, BINGO_MAX_NUMBER)
            val next = if (n in state.drawn) state.drawn else state.drawn + n
            val wins = checkWins(b, next, patterns)
            state.copy(drawn = next, lastDrawn = n, wins = wins, errorMessage = null)
        }

        is GameAction.Fail -> state.copy(errorMessage = action.message)
    }
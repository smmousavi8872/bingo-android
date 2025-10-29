package com.smmousavi.developer.lvtgames.feature.game

import com.smmousavi.developer.lvtgames.core.model.domain.game.BINGO_MAX_NUMBER
import com.smmousavi.developer.lvtgames.core.model.domain.game.BINGO_MIN_NUMBER


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smmousavi.developer.lvtgames.core.model.domain.game.GameBoard
import com.smmousavi.developer.lvtgames.domain.game.usecase.GameBoardUseCase
import com.smmousavi.developer.lvtgames.feature.game.uimodel.DrawMode
import com.smmousavi.developer.lvtgames.feature.game.uimodel.GameAction
import com.smmousavi.developer.lvtgames.feature.game.uimodel.GameUiModel
import com.smmousavi.developer.lvtgames.feature.game.uimodel.reduce
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(private val useCase: GameBoardUseCase) : ViewModel() {

    private val _state = MutableStateFlow(GameUiModel())
    val state: StateFlow<GameUiModel> = _state
    private var autoJob: Job? = null
    private val rng = Random(System.currentTimeMillis())

    fun start(gameBoard: GameBoard, mode: DrawMode = DrawMode.Manual) {
        stopAuto()
        _state.update { s -> reduce(s, GameAction.Start(gameBoard, mode), useCase.patterns) }
        if (mode is DrawMode.Auto) startAuto(mode.intervalMillis)
    }

    fun pause() {
        stopAuto()
        _state.update { s -> reduce(s, GameAction.Pause, useCase.patterns) }
    }

    fun resume() {
        val st = _state.value
        if (st.mode is DrawMode.Auto) startAuto(st.mode.intervalMillis)
        _state.update { s -> reduce(s, GameAction.Resume, useCase.patterns) }
    }

    fun reset() {
        stopAuto()
        _state.update { s -> reduce(s, GameAction.Reset, useCase.patterns) }
    }

    fun draw(number: Int) {
        _state.update { s -> reduce(s, GameAction.DrawOne(number), useCase.patterns) }
    }

    fun drawRandom() {
        val st = _state.value
        val pool = (BINGO_MIN_NUMBER..BINGO_MAX_NUMBER).filterNot { it in st.drawn }
        if (pool.isEmpty()) return
        draw(pool.random(rng))
    }

    private fun startAuto(interval: Long) {
        autoJob = viewModelScope.launch {
            while (true) {
                delay(interval)
                drawRandom()
            }
        }
    }

    private fun stopAuto() {
        autoJob?.cancel()
        autoJob = null
    }
}
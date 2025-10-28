package com.smmousavi.developer.lvtgames.core.model.domain.game

typealias Pos = Pair<Int, Int>

data class GameBoard(val matrix: List<List<Int?>>) {
    val rows get() = matrix.size
    val cols get() = matrix.firstOrNull()?.size ?: 0
    fun valueAt(pos: Pos): Int? = matrix[pos.first][pos.second]
    fun positions(): Sequence<Pos> = sequence {
        for (r in 0 until rows) for (c in 0 until cols) yield(r to c)
    }
    companion object {
        fun fromMatrix(matrix: List<List<Int?>>): GameBoard = GameBoard(matrix)
    }
}

const val BINGO_MIN_NUMBER = 1
const val BINGO_MAX_NUMBER = 90

fun GameBoard.markedPositions(drawn: Set<Int>): Set<Pos> =
    positions().filter { valueAt(it)?.let { v -> v != -1 && drawn.contains(v) } == true }.toSet()

interface WinPattern {
    val name: String
    fun positions(gameBoard: GameBoard): List<Set<Pos>>
}

object RowsPattern : WinPattern {
    override val name = "Row"
    override fun positions(gameBoard: GameBoard) =
        (0 until gameBoard.rows).map { r -> (0 until gameBoard.cols).map { c -> r to c }.toSet() }
}

object FourCornersPattern : WinPattern {
    override val name = "Four Corners"
    override fun positions(gameBoard: GameBoard): List<Set<Pos>> {
        if (gameBoard.rows == 0 || gameBoard.cols == 0) return emptyList()
        return listOf(
            setOf(0 to 0, 0 to (gameBoard.cols - 1), (gameBoard.rows - 1) to 0, (gameBoard.rows - 1) to (gameBoard.cols - 1))
        )
    }
}

object FullHousePattern : WinPattern {
    override val name = "Full House"
    override fun positions(gameBoard: GameBoard): List<Set<Pos>> =
        listOf(gameBoard.positions().filter { gameBoard.valueAt(it) != null && gameBoard.valueAt(it) != -1 }.toSet())
}

data class WinCheck(val achieved: Map<String, List<Set<Pos>>>)

fun checkWins(gameBoard: GameBoard, drawn: Set<Int>, patterns: List<WinPattern>): WinCheck {
    val marked = gameBoard.markedPositions(drawn)
    val achieved = buildMap {
        patterns.forEach { p ->
            val ok = p.positions(gameBoard).filter { set -> set.all { it in marked } }
            if (ok.isNotEmpty()) put(p.name, ok)
        }
    }
    return WinCheck(achieved)
}
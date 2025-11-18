package com.smmousavi.developer.lvtgames.core.model.domain.game

typealias Pos = Pair<Int, Int>

data class Board(val matrix: List<List<Int?>>) {
    val rows get() = matrix.size
    val cols get() = matrix.firstOrNull()?.size ?: 0
    fun valueAt(pos: Pos): Int? = matrix[pos.first][pos.second]
    fun positions(): Sequence<Pos> = sequence {
        for (r in 0 until rows) for (c in 0 until cols) yield(r to c)
    }
    companion object {
        fun fromMatrix(matrix: List<List<Int?>>): Board = Board(matrix)
    }
}

const val BINGO_MIN_NUMBER = 1
const val BINGO_MAX_NUMBER = 90

fun Board.markedPositions(drawn: Set<Int>): Set<Pos> =
    positions().filter { valueAt(it)?.let { v -> v != -1 && drawn.contains(v) } == true }.toSet()

interface WinPattern {
    val name: String
    fun positions(board: Board): List<Set<Pos>>
}

object RowsPattern : WinPattern {
    override val name = "Row"
    override fun positions(board: Board) =
        (0 until board.rows).map { r -> (0 until board.cols).map { c -> r to c }.toSet() }
}

object FourCornersPattern : WinPattern {
    override val name = "Four Corners"
    override fun positions(board: Board): List<Set<Pos>> {
        if (board.rows == 0 || board.cols == 0) return emptyList()
        return listOf(
            setOf(0 to 0, 0 to (board.cols - 1), (board.rows - 1) to 0, (board.rows - 1) to (board.cols - 1))
        )
    }
}

object FullHousePattern : WinPattern {
    override val name = "Full House"
    override fun positions(board: Board): List<Set<Pos>> =
        listOf(board.positions().filter { board.valueAt(it) != null && board.valueAt(it) != -1 }.toSet())
}

data class WinCheck(val achieved: Map<String, List<Set<Pos>>>)

fun checkWins(board: Board, drawn: Set<Int>, patterns: List<WinPattern>): WinCheck {
    val marked = board.markedPositions(drawn)
    val achieved = buildMap {
        patterns.forEach { p ->
            val ok = p.positions(board).filter { set -> set.all { it in marked } }
            if (ok.isNotEmpty()) put(p.name, ok)
        }
    }
    return WinCheck(achieved)
}
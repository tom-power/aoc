package aoc22

import aoc22.Day22Domain.State
import aoc22.Day22Parser.toBoard
import aoc22.Day22Parser.toPath
import common.Space2D.Direction.*
import common.Space2D.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22BoardTest {

    private fun String.toPath() = listOf("", this).toPath()

    private val openInput = listOf(
        "....",
        "....",
        "....",
        "....",
        "",
    )

    private val openBoard = openInput.toBoard()

    @Test
    fun `can move with rights`() {
        assertEquals(State(Point(x = 0, y = 0), Up), openBoard.follow("3R3R3R3".toPath()))
//        board.monitor.print()
//        ^..>
//        ....
//        ....
//        <..v
    }

    @Test
    fun `can move with lefts`() {
        assertEquals(State(Point(x = 0, y = 0), Left), openBoard.follow("R3L3L3L3".toPath()))
//         board.monitor.print()
//         v..^
//         ....
//         ....
//         v..>
    }

}
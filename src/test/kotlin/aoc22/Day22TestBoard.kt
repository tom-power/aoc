package aoc22

import aoc22.Day22Domain.State
import aoc22.Day22Parser.toBoard
import aoc22.Day22Parser.toPath
import aoc22.Space2D.Direction.*
import aoc22.Space2D.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22TestBoard {

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

    private val offsetInput = listOf(
        "....",
        "....",
        "....",
        "....",
        "   ....",
        "   ....",
        "   ....",
        "",
    )

    private val offsetBoard = offsetInput.toBoard()

    @Test
    fun `can loop offset`() {
        assertEquals(State(Point(x = 6, y = -6), Up), offsetBoard.follow("3R5R1R2".toPath()))
//        offsetBoard.monitor.print()
//        ...v
//        ....
//        ....
//           ....
//           ....
//           <..^
//           ...^
    }



    private val wallInput = listOf(
        "...#",
        "....",
        "....",
        "....",
        "",
    )

    private val wallBoard = wallInput.toBoard()

    @Test
    fun `can loop wall`() {
        assertEquals(State(Point(x = 2, y = 0), Right), wallBoard.follow("3".toPath())) // blocked by wall
        assertEquals(State(Point(x = 0, y = 0), Left), wallBoard.follow("RR1".toPath())) // blocked by wall on loop
    }
}
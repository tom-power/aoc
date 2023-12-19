package aoc22

import aoc22.Day22Domain.State
import aoc22.Day22Parser.toBoard
import aoc22.Day22Parser.toPath
import aoc22.Day22DomainWrap.withFlatWrap
import common.Space2D.Direction.*
import common.Space2D.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22BoardFlatTest {

    private fun String.toPath() = listOf("", this).toPath()

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

    private val offsetBoard = offsetInput.toBoard().withFlatWrap()

    @Test
    fun `can wrap offset`() {
        assertEquals(State(Point(x = 6, y = -6), North), offsetBoard.follow("3R5R1R2".toPath()))
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

    private val wallBoard = wallInput.toBoard().withFlatWrap()

    @Test
    fun `can wrap wall`() {
        assertEquals(State(Point(x = 2, y = 0), East), wallBoard.follow("3".toPath())) // blocked by wall
        assertEquals(State(Point(x = 0, y = 0), West), wallBoard.follow("RR1".toPath())) // blocked by wall on loop
    }
}
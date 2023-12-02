package aoc22

import aoc22.Day14Domain.Cave
import aoc22.Day14Parser.toCave
import common.Space2D.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14ParserTest {

    @Test
    fun `parse a cave`() {
        val expected = Cave(
            rock = mutableSetOf(
                Point(x = 498, y = -4),
                Point(x = 498, y = -5),
                Point(x = 498, y = -6),
                Point(x = 497, y = -6),
                Point(x = 496, y = -6)
            ),
            sandStartsFrom = Point(x = 500, y = 0),
            fallingSand = Point(x = 500, y = 0),
        )

        val actual = listOf("498,4 -> 498,6 -> 496,6").toCave()

        assertEquals(expected, actual)
    }
}
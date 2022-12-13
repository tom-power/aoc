package aoc22

import aoc22.Day13Parser.toPackets
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day13ParserTest {

    @Test
    @Disabled
    fun `parse input example`() {
        assertEquals(exampleAsPackets, Input.readInputWithFallback("Day13_example", "aoc22").toPackets())
    }

    private val exampleAsPackets = listOf(
        listOf(1, 1, 3, 1, 1), listOf(1, 1, 5, 1, 1),
        listOf(listOf(1), listOf(2, 3, 4)), listOf(listOf(1), 4),
        listOf(9), listOf(listOf(8, 7, 6)),
        listOf(listOf(4, 4), 4, 4), listOf(listOf(4, 4), 4, 4, 4),
        listOf(7, 7, 7, 7), listOf(7, 7, 7),
        listOf(), listOf(3),
        listOf(listOf<Any>(listOf<Any>())), listOf(listOf<Any>()),
        listOf(1, listOf(2, listOf(3, listOf(4, listOf(5, 6, 7)))), 8, 9),
        listOf(1, listOf(2, listOf(3, listOf(4, listOf(5, 6, 0)))), 8, 9)
    )

}
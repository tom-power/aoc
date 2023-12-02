package aoc22

import aoc22.Day08.part1
import aoc22.Day08.part2
import common.readInput
import common.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day08Test {

    @Test
    fun `part one example`() {
        assertEquals(21, Day08.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(1669, Day08.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(8, Day08.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(331344, Day08.readInput().part2())
    }
}
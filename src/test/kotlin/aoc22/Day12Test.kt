package aoc22

import aoc22.Day12.part1
import aoc22.Day12.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Test {

    @Test
    fun `part one example`() {
        assertEquals(31, Day12.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(481, Day12.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(29, Day12.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(480, Day12.readInput().part2())
    }
}
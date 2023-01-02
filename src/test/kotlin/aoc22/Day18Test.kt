package aoc22

import aoc22.Day18.part1
import aoc22.Day18.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day18Test {

    @Test
    fun `part one example`() {
        assertEquals(64, Day18.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(4444, Day18.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(0, Day18.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(0, Day18.readInput().part2())
    }
}
package aoc22

import aoc22.Day11.part1
import aoc22.Day11.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {

    @Test
    fun `part one example`() {
        assertEquals(10605, Day11.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(99840, Day11.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(0, Day11.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(0, Day11.readInput().part2())
    }
}
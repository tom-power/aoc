package aoc22

import aoc22.Day14.part1
import aoc22.Day14.part2
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14Test {
    @Test
    fun `part one example`() {
        assertEquals(24, Day14.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(838, Day14.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(93, Day14.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(27539, Day14.readInput().part2())
    }
}
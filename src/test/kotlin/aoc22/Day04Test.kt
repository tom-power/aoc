package aoc22

import aoc22.Day04.part1
import aoc22.Day04.part2
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day04Test {
    @Test
    fun `part one example`() {
        assertEquals(2, Day04.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(576, Day04.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(4, Day04.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(905, Day04.readInput().part2())
    }
}
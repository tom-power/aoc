package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day06.part1
import aoc23.Day06.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day06Test {
    @Test
    fun `part one example`() {
        assertEquals(288, Day06.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(1195150, Day06.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(71503, Day06.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(42550411, Day06.readInput().part2())
    }
}
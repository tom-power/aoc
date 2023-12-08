package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day04.part1
import aoc23.Day04.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day04Test {
    @Test
    fun `part one example`() {
        assertEquals(13, Day04.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(20829, Day04.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(30, Day04.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(12648035, Day04.readInput().part2())
    }
}
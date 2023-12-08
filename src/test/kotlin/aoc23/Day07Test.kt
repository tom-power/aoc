package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day07.part1
import aoc23.Day07.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day07Test {
    @Test
    fun `part one example`() {
        assertEquals(6440, Day07.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(251136060, Day07.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(5905, Day07.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(249400220, Day07.readInput().part2())
    }
}
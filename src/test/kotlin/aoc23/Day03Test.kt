package aoc23

import common.readInput
import common.readInputExample
import aoc23.Day03.part1
import aoc23.Day03.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03Test {

    @Test
    fun `part one example`() {
        assertEquals(4361, Day03.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(533784, Day03.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(467835, Day03.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(78826761, Day03.readInput().part2())
    }
}
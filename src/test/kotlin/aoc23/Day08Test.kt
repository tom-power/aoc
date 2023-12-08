package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day08.part1
import aoc23.Day08.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day08Test {
    @Test
    fun `part one example`() {
        assertEquals(6, Day08.readInput("_example1").part1())
    }

    @Test
    fun `part one`() {
        assertEquals(20093, Day08.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(6, Day08.readInput("_example2").part2())
    }

    @Test
    fun `part two`() {
        assertEquals(22103062509257, Day08.readInput().part2())
    }
}
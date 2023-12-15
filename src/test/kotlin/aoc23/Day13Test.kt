package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day13.part1
import aoc23.Day13.part2
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day13Test {
    @Test
    fun `part one example`() {
        assertEquals(405, Day13.readInputExample().part1())
    }

    @Test
    fun `part one example 1`() {
        assertEquals(2600, Day13.readInput("_example1").part1())
    }

    @Test
    fun `part one example 2`() {
        assertEquals(13, Day13.readInput("_example2").part1())
    }

    @Test
    fun `part one example 3`() {
        assertEquals(12, Day13.readInput("_example3").part1())
    }

    @Test
    fun `part one`() {
        assertEquals(36448, Day13.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(400, Day13.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(0, Day13.readInput().part2())
    }
}
package aoc23

import aoc23.Day01.part1
import aoc23.Day01.part2
import common.Input.readInput
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day01Test {
    @Test
    fun `part one example`() {
        assertEquals(142, Day01.readInput("_example1").part1())
    }

    @Test
    fun `part one`() {
        assertEquals(54953, Day01.readInput().part1())
    }

    @Test
    fun `part two example`() {
         assertEquals(281, Day01.readInput("_example2").part2())
    }

    @Test
    fun `part two`() {
        assertEquals(53868, Day01.readInput().part2())
    }
}
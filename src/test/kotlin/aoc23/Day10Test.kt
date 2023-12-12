package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day10.part1
import aoc23.Day10.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun `part one example`() {
        assertEquals(8, Day10.readInput("_example1").part1())
    }

    @Test
    fun `part one`() {
        assertEquals(6714, Day10.readInput().part1())
    }

    @Nested
    inner class Day10TestPart2 {
        @Test
        fun `part two example 2`() {
            assertEquals(4, Day10.readInput("_example2").part2())
        }

        @Test
        fun `part two example 2a`() {
            assertEquals(4, Day10.readInput("_example2a").part2())
        }

        @Test
        fun `part two example 3`() {
            assertEquals(8, Day10.readInput("_example3").part2())
        }

        @Test
        fun `part two example 4`() {
            assertEquals(10, Day10.readInput("_example4").part2())
        }

        @Test
        fun `part two`() {
            assertEquals(429, Day10.readInput().part2())
        }
    }

}
package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day17.part1
import aoc23.Day17.part2
import common.Misc.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.time.measureTime

class Day17Test {
    @Nested
    inner class PartOne {
        @Test
        fun `part one example`() {
            assertEquals(102, Day17.readInputExample().part1())
        }

        @Test
        fun `part one`() {
            assertEquals(1128, Day17.readInput().part1())
        }
    }

    @Nested
    inner class PartOneExamples {
        @Test
        fun `part one example 1`() {
            assertEquals(6 , Day17.readInput("_example1").part1())
        }

        @Test
        fun `part one example 2`() {
            assertEquals(12, Day17.readInput("_example2").part1())
        }

        @Test
        fun `part one example 3`() {
            assertEquals(16 , Day17.readInput("_example3").part1())
        }
    }

    @Nested
    inner class PartTwo {
        @Test
        fun `part two example`() {
            assertEquals(0, Day17.readInputExample().part2())
        }

        @Test
        fun `part two`() {
            assertEquals(0, Day17.readInput().part2())
        }
    }
}
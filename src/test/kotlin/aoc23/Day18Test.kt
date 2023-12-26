package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day18.part1
import aoc23.Day18.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day18Test {
    @Nested
    inner class PartOne {
        @Test
        fun `part one example`() {
            assertEquals(62, Day18.readInputExample().part1())
        }

        @Test
        fun `part one`() {
            assertEquals(47045, Day18.readInput().part1())
        }
    }

    @Nested
    inner class PartTwo {
        @Test
        fun `part two example`() {
            assertEquals(952408144115L, Day18.readInputExample().part2())
        }

        @Test
        fun `part two`() {
            assertEquals(147839570293376, Day18.readInput().part2())
        }
    }
}
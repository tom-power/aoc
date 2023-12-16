package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day14.part1
import aoc23.Day14.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day14Test {
    @Nested
    inner class PartOne {
        @Test
        fun `part one example`() {
            assertEquals(136, Day14.readInputExample().part1())
        }

        @Test
        fun `part one`() {
            assertEquals(110565, Day14.readInput().part1())
        }
    }

    @Nested
    inner class PartTwo {
        @Test
        fun `part two example`() {
            assertEquals(0, Day14.readInputExample().part2())
        }

        @Test
        fun `part two`() {
            assertEquals(0, Day14.readInput().part2())
        }
    }
}
package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day15.part1
import aoc23.Day15.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day15Test {
    @Nested
    inner class PartOne {
        @Test
        fun `part one example`() {
            assertEquals(1320, Day15.readInputExample().part1())
        }

        @Test
        fun `part one`() {
            assertEquals(507666, Day15.readInput().part1())
        }
    }

    @Nested
    inner class PartTwo {
        @Test
        fun `part two example`() {
            assertEquals(145, Day15.readInputExample().part2())
        }

        @Test
        fun `part two`() {
            assertEquals(233537, Day15.readInput().part2())
        }
    }
}
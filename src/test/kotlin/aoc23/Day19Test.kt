package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day19.part1
import aoc23.Day19.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day19Test {
    @Nested
    inner class PartOne {
        @Test
        fun `part one example`() {
            assertEquals(19114, Day19.readInputExample().part1())
        }

        @Test
        fun `part one`() {
            assertEquals(398527, Day19.readInput().part1())
        }
    }

    @Nested
    inner class PartTwo {
        @Test
        fun `part two example`() {
            assertEquals(0, Day19.readInputExample().part2())
        }

        @Test
        fun `part two`() {
            assertEquals(0, Day19.readInput().part2())
        }
    }
}
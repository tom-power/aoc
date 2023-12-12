package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day11.part1
import aoc23.Day11.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day11Test {
    @Nested
    inner class Day11TestPart1 {
        @Test
        fun `part one example`() {
            assertEquals(374, Day11.readInputExample().part1())
        }

        @Test
        fun `part one`() {
            assertEquals(9957702, Day11.readInput().part1())
        }
    }

    @Nested
    inner class Day11TestPart2 {
        @Test
        fun `part two example 10`() {
            assertEquals(1030, Day11.readInputExample().part2(emptySpaceMultiplier = 10))
        }

        @Test
        fun `part two example 100`() {
            assertEquals(8410, Day11.readInputExample().part2(emptySpaceMultiplier = 100))
        }

        @Test
        fun `part two`() {
            assertEquals(512240933238, Day11.readInput().part2(emptySpaceMultiplier = 1000000))
        }
    }
}
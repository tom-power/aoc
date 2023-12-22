package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day16.part1
import aoc23.Day16.part2
import common.Space2D
import common.Space2D.Direction.*
import common.Space2D.Parser.toPointToChars
import common.Space2D.lastIn
import common.Space2D.max
import common.Space2D.min
import common.Space2D.nextIn
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day16Test {
    @Nested
    inner class PartOne {
        @Test
        fun `part one example`() {
            assertEquals(46, Day16.readInputExample().part1())
        }

        @Test
        fun `part one`() {
            assertEquals(8034, Day16.readInput().part1())
        }
    }

    @Nested
    inner class PartTwo {
        @Test
        fun `part two example`() {
            assertEquals(51, Day16.readInputExample().part2())
        }

        @Test
        fun `part two`() {
            assertEquals(8225, Day16.readInput().part2())
        }
    }
}
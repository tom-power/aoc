package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day14.part1
import aoc23.Day14.part2
import aoc23.Day14Parser.toParabolicReflectorDish
import common.Misc.log
import common.Space2D
import common.Space2D.toLoggable
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
            assertEquals(64, Day14.readInputExample().part2())
        }

        @Test
        fun `part two`() {
            assertEquals(89845, Day14.readInput().part2())
        }
    }

    @Nested
    inner class PartTwoCycles {
        @Test
        fun `part two example cycles 1`() {
            assertEquals(loadFrom(example = "_exampleCycles1"), loadFromExample(maxSpinCycles = 1))
        }

        @Test
        fun `part two example cycles 2`() {
            assertEquals(loadFrom(example = "_exampleCycles2"), loadFromExample(maxSpinCycles = 2))
        }

        @Test
        fun `part two example cycles 3`() {
            assertEquals(loadFrom(example = "_exampleCycles3"), loadFromExample(maxSpinCycles = 3))
        }

        private fun loadFromExample(maxSpinCycles: Int) =
            Day14.readInputExample()
                .toParabolicReflectorDish(maxSpinCycles)
                .apply { spinCycles() }
                .rockSpaceMap
//                .also { "actual".log() + it.printMap() }

        private fun loadFrom(example: String) =
            Day14.readInput(example)
                .toParabolicReflectorDish()
                .rockSpaceMap
//                .also { "example".log() + it.printMap() }

        private fun MutableMap<Space2D.Point, Char>.printMap() {
            keys.toLoggable(highlight = filterValues { it == 'O' }.keys).log()
        }
    }
}
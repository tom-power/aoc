package aoc22

import aoc22.Day19.part1
import aoc22.Day19.part2
import common.readInput
import common.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day19Test {

    @Test
    fun `part one example`() {
        assertEquals(33, Day19.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(1147, Day19.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(3472, Day19.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(3080, Day19.readInput().part2())
    }
}
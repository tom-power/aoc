package aoc22

import aoc22.Day13.part1
import aoc22.Day13.part2
import common.readInput
import common.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13Test {

    @Test
    fun `part one example`() {
        assertEquals(13, Day13.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(5292, Day13.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(140, Day13.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(23868, Day13.readInput().part2())
    }
}
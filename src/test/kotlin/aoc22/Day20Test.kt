package aoc22

import aoc22.Day20.part1
import aoc22.Day20.part2
import common.readInput
import common.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day20Test {

    @Test
    fun `part one example`() {
        assertEquals(3, Day20.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(10707, Day20.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(1623178306, Day20.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(2488332343098, Day20.readInput().part2())
    }
}
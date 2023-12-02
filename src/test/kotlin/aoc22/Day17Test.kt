package aoc22

import aoc22.Day17.part1
import aoc22.Day17.part2
import common.readInput
import common.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day17Test {

    @Test
    fun `part one example`() {
        assertEquals(3068, Day17.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(3144, Day17.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(1514285714288, Day17.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(1565242165201, Day17.readInput().part2())
    }
}
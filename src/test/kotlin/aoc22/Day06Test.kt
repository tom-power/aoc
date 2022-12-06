package aoc22

import aoc22.Day06.part1
import aoc22.Day06.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day06Test {

    @Test
    fun `part one example`() {
        assertEquals(7, Day06.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(1080, Day06.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(19, Day06.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(3645, Day06.readInput().part2())
    }
}
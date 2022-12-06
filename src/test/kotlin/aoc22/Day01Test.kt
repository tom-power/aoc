package aoc22

import aoc22.Day01.part1
import aoc22.Day01.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day01Test {

    @Test
    fun `part one example`() {
        assertEquals(24000, Day01.readDaysInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(75501, Day01.readDaysInput().part1())
    }

    @Test
    fun `part two`() {
        assertEquals(215594, Day01.readDaysInput().part2())
    }
}
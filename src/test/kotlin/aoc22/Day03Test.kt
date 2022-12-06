package aoc22

import aoc22.Day03.part1
import aoc22.Day03.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03Test {

    @Test
    fun `part one example`() {
        assertEquals(Day03.readDaysInputExample().part1(), 157)
    }

    @Test
    fun `part one`() {
        assertEquals(Day03.readDaysInput().part1(), 7553)
    }

    @Test
    fun `part two example`() {
        assertEquals(Day03.readDaysInputExample().part2(), 70)
    }

    @Test
    fun `part two`() {
        assertEquals(Day03.readDaysInput().part2(), 2758)
    }
}
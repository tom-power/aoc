package aoc22

import aoc22.Day03
import aoc22.Day03.part1
import aoc22.Day03.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03Test {

    @Test
    fun `part one test`() {
        assertEquals(Day03.readDaysInputTest().part1(), 157)
    }

    @Test
    fun `part one`() {
        assertEquals(Day03.readDaysInput().part1(), 7817)
    }

    @Test
    fun `part two test`() {
        assertEquals(Day03.readDaysInputTest().part2(), 70)
    }

    @Test
    fun `part two`() {
        assertEquals(Day03.readDaysInput().part2(), 2444)
    }
}
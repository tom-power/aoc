package aoc22

import aoc22.Day01
import aoc22.Day01.part1
import aoc22.Day01.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day01Test {

    @Test
    fun `part one test`() {
        assertEquals(24000, Day01.readDaysInputTest().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(70613, Day01.readDaysInput().part1())
    }

    @Test
    fun `part two`() {
        assertEquals(205805, Day01.readDaysInput().part2())
    }
}
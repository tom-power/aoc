package aoc22

import aoc22.Day04.part1
import aoc22.Day04.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day04Test {

    @Test
    fun `part one example`() {
        assertEquals(2, Day04.readDaysInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(431, Day04.readDaysInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(4, Day04.readDaysInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(823, Day04.readDaysInput().part2())
    }
}
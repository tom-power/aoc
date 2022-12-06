package aoc22

import aoc22.Day00.part1
import aoc22.Day00.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day00Test {

    @Test
    fun `part one example`() {
        assertEquals(0, Day00.readDaysInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(0, Day00.readDaysInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(0, Day00.readDaysInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(0, Day00.readDaysInput().part2())
    }
}
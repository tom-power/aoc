package aoc22

import aoc22.Day02.part1
import aoc22.Day02.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {

    @Test
    fun `part one example`() {
        assertEquals(15, Day02.readDaysInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(13009, Day02.readDaysInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(12, Day02.readDaysInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(10398, Day02.readDaysInput().part2())
    }
}
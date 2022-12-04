package aoc22

import aoc22.Day02.part1
import aoc22.Day02.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {

    @Test
    fun `part one test`() {
        assertEquals(15, Day02.readDaysInputTest().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(13009, Day02.readDaysInput().part1())
    }

    @Test
    fun `part two test`() {
        assertEquals(12, Day02.readDaysInputTest().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(10398, Day02.readDaysInput().part2())
    }
}
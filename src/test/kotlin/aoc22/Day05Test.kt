package aoc22

import aoc22.Day05.part1
import aoc22.Day05.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05Test {

    @Test
    fun `part one example`() {
        assertEquals("CMZ", Day05.readDaysInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals("QNNTGTPFN", Day05.readDaysInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals("MCD", Day05.readDaysInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals("GGNPJBTTR", Day05.readDaysInput().part2())
    }
}
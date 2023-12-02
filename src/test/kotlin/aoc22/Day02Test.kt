package aoc22

import aoc22.Day02.part1
import aoc22.Day02.part2
import common.readInput
import common.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {

    @Test
    fun `part one example`() {
        assertEquals(15, Day02.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(12586, Day02.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(12, Day02.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(13193, Day02.readInput().part2())
    }
}
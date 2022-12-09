package aoc22

import aoc22.Day09.part1
import aoc22.Day09.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day09Test {

    @Test
    fun `part one example`() {
        assertEquals(13, Day09.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(6642, Day09.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(1, Day09.readInputExample().part2())
    }

    @Test
    fun `part two example 2`() {
        assertEquals(36, Day09.readInputExample2().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(2765, Day09.readInput().part2())
    }
}
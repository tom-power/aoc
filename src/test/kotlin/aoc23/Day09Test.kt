package aoc23

import aoc23.Day09.part1
import aoc23.Day09.part2
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day09Test {
    @Test
    fun `part one example`() {
        assertEquals(114, Day09.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(1819125966, Day09.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(2, Day09.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(1140, Day09.readInput().part2())
    }
}
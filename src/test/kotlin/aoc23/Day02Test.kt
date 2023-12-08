package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day02.part1
import aoc23.Day02.part2
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {
    @Test
    fun `part one example`() {
        assertEquals(8, Day02.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(2727, Day02.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(2286, Day02.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(56580, Day02.readInput().part2())
    }
}
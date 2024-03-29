package aoc22

import aoc22.Day24.part1
import aoc22.Day24.part2
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class Day24Test {
    @Test
    fun `part one example`() {
        assertEquals(18, Day24.readInputExample().part1())
    }

    @Test
    @Tag("slow")
    fun `part one`() {
        assertEquals(297, Day24.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(54, Day24.readInputExample().part2())
    }

    @Test
    @Tag("slow")
    fun `part two`() {
        assertEquals(856, Day24.readInput().part2())
    }
}
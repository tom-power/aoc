package aoc22

import aoc22.Day16.part1
import aoc22.Day16.part2
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class Day16Test {
    @Test
    fun `part one example`() {
        assertEquals(1651, Day16.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(2114, Day16.readInput().part1())
    }

    @Test
    @Tag("slow")
    fun `part two example`() {
        assertEquals(1707, Day16.readInputExample().part2())
    }

    @Test
    @Tag("slow")
    fun `part two`() {
        assertEquals(2666, Day16.readInput().part2())
    }
}
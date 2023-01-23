package aoc22

import aoc22.Day23.part1
import aoc22.Day23.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day23Test {

    @Test
    fun `part one example`() {
        assertEquals(110, Day23.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(3987, Day23.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(0, Day23.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(0, Day23.readInput().part2())
    }
}
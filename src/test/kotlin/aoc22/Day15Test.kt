package aoc22

import aoc22.Day15.part1
import aoc22.Day15.part2
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class Day15Test {
    @Test
    fun `part one example`() {
        assertEquals(26, Day15.readInputExample().part1(y = 10))
    }

    @Test
    @Tag("slow")
    fun `part one`() {
        assertEquals(5367037, Day15.readInput().part1(y = 2000000))
    }

    @Test
    fun `part two example`() {
        assertEquals(56000011, Day15.readInputExample().part2(gridMax = 20))
    }

    @Test
    @Tag("slow")
    fun `part two`() {
        assertEquals(11914583249288, Day15.readInput().part2(gridMax = 4000000))
    }
}
package aoc22

import aoc22.Day22.part1
import aoc22.Day22.part2
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22Test {
    @Test
    fun `part one example`() {
        assertEquals(6032, Day22.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(50412, Day22.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(5031, Day22.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(130068, Day22.readInput().part2())
    }
}
package aoc22

import aoc22.Day07.part1
import aoc22.Day07.part2
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day07Test {
    @Test
    fun `part one example`() {
        assertEquals(95437, Day07.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(1350966, Day07.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(24933642, Day07.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(6296435, Day07.readInput().part2())
    }
}
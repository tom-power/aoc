package aoc23

import common.Input.readInput
import common.Input.readInputExample
import aoc23.Day05.part1
import aoc23.Day05.part2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05Test {
    @Test
    fun `part one example`() {
        assertEquals(35, Day05.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(993500720, Day05.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(46, Day05.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(4917124, Day05.readInput().part2())
    }
}
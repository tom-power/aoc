package aoc22

import aoc22.Day21.part1
import aoc22.Day21.part2
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day21Test {
    @Test
    fun `part one example`() {
        assertEquals(152, Day21.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals(49288254556480, Day21.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(301, Day21.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(3558714869436, Day21.readInput().part2())
    }
}
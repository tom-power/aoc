package aoc23

import common.readInput
import common.readInputExample
import aoc23.Day02.part1
import aoc23.Day02.part2
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day02Test {

    @Test
    fun `part one example`() {
        Assertions.assertEquals(8, Day02.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        Assertions.assertEquals(2727, Day02.readInput().part1())
    }

    @Test
    fun `part two example`() {
        Assertions.assertEquals(2286, Day02.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        Assertions.assertEquals(56580, Day02.readInput().part2())
    }
}
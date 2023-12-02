package aoc23

import aoc23.Day01.part1
import aoc23.Day01.part2
import common.readInput
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day01Test {

    @Test
    fun `part one example`() {
        Assertions.assertEquals(142, Day01.readInput("_example_part1").part1())
    }

    @Test
    fun `part one`() {
        Assertions.assertEquals(54953, Day01.readInput().part1())
    }

    @Test
    fun `part two example`() {
         Assertions.assertEquals(281, Day01.readInput("_example_part2").part2())
    }

    @Test
    fun `part two`() {
        Assertions.assertEquals(53868, Day01.readInput().part2())
    }
}
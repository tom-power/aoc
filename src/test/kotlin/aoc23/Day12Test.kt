package aoc23

import aoc23.Day12.part1
import aoc23.Day12.part2
import aoc23.Day12Parser.unfoldFiveTimes
import common.Input.readInput
import common.Input.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Test {
    @Test
    fun `part one example`() {
        assertEquals(21, Day12.readInputExample().part1())
    }

    @Test
    fun `part one test 1`() {
        assertEquals(2, listOf("???????#?#?.??#. 7,2").part1())
    }

    @Test
    fun `part one test 2`() {
        assertEquals(15, listOf(".???#??.???????. 4,3").part1())
    }

    @Test
    fun `part one`() {
        assertEquals(7344, Day12.readInput().part1())
    }

    @Test
    fun `unfold five times`() {
        assertEquals(".#?.#?.#?.#?.# 1,1,1,1,1", ".# 1".unfoldFiveTimes())
        assertEquals(
            "???.###????.###????.###????.###????.### 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3",
            "???.### 1,1,3".unfoldFiveTimes()
        )
    }

    @Test
    fun `part two example`() {
        assertEquals(525152, Day12.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(1088006519007, Day12.readInput().part2())
    }
}
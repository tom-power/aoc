package template

import template.Day00.part1
import template.Day00.part2
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day00Test {

    @Test
    fun `part one example`() {
        Assertions.assertEquals(0, Day00.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        Assertions.assertEquals(0, Day00.readInput().part1())
    }

    @Test
    fun `part two example`() {
        Assertions.assertEquals(0, Day00.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        Assertions.assertEquals(0, Day00.readInput().part2())
    }
}
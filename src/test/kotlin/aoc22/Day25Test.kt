package aoc22

import aoc22.Day25.part1
import common.readInput
import common.readInputExample
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day25Test {

    @Test
    fun `part one example`() {
        assertEquals("2=-1=0", Day25.readInputExample().part1())
    }

    @Test
    fun `part one`() {
        assertEquals("20=02=120-=-2110-0=1", Day25.readInput().part1())
    }
}
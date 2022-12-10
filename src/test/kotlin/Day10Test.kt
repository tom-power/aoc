package aoc22

import aoc22.Day10.part1
import aoc22.Day10.part2
import aoc22.Day10Solution.Cycle
import aoc22.Day10Solution.Register
import aoc22.Misc.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {

    @Test
    fun `part one example`() {
        assertEquals(13140, Day10.readInputExample().part1())
    }

    @Test
    fun `part 1 cycles`() {
        """
            During the first cycle, X is 1. After the first cycle, the noop instruction finishes execution, doing nothing.
            During the second cycle, X is still 1.
            During the third cycle, X is still 1. After the third cycle, the addx 3 instruction finishes execution, setting X to 4.
            At the start of the fourth cycle, the addx -5 instruction begins execution. During the fourth cycle, X is still 4.
            During the fifth cycle, X is still 4. After the fifth cycle, the addx -5 instruction finishes execution, setting X to -1.
        """.trimIndent()

        val input = listOf("noop", "addx 3", "addx -5")
        val expected = listOf(
            Cycle(v = 1, registerDuring = Register(x = 1), registerAfter = Register(x = 1)),
            Cycle(v = 2, registerDuring = Register(x = 1), registerAfter = Register(x = 1)),
            Cycle(v = 3, registerDuring = Register(x = 1), registerAfter = Register(x = 4)),
            Cycle(v = 4, registerDuring = Register(x = 4), registerAfter = Register(x = 4)),
            Cycle(v = 5, registerDuring = Register(x = 4), registerAfter = Register(x = -1)),
        )
        assertEquals(
            expected,
            with(Day10Solution) { input.toInstructions().toRegisters().toCycles().log() }
        )
    }

    @Test
    fun `part one`() {
        assertEquals(11220, Day10.readInput().part1())
    }

    @Test
    fun `part two example`() {
        assertEquals(0, Day10.readInputExample().part2())
    }

    @Test
    fun `part two`() {
        assertEquals(0, Day10.readInput().part2())
    }
}
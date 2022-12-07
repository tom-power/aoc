package aoc22

import aoc22.Day00Solution.part1Day00
import aoc22.Day00Solution.part2Day00
import aoc22.Misc.log

object Day00 : Day<String, Int> {
    override fun List<String>.part1(): Int = part1Day00()

    override fun List<String>.part2(): Int = part2Day00()
}

object Day00Solution {
    fun List<String>.part1Day00(): Int =
        map { it.split(",") }.log()
            .let { 0 }

    fun List<String>.part2Day00(): Int =
        map { it.split(",") }.log()
            .let { 0 }
}

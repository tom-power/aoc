package aoc22

import aoc22.Day18Runner.countExposedSides
import aoc22.Day18Solution.part1Day18
import aoc22.Day18Solution.part2Day18
import aoc22.Misc.log
import aoc22.Space3D.Parser.toPoint3D

object Day18 : Day {
    fun List<String>.part1(): Int = part1Day18()

    fun List<String>.part2(): Int = part2Day18()
}

object Day18Solution {
    fun List<String>.part1Day18(): Int =
        map { it.toPoint3D() }
            .countExposedSides()

    fun List<String>.part2Day18(): Int =
        map { it.split(",") }.log()
            .let { 0 }
}

object Day18Runner {
    fun List<Space3D.Point3D>.countExposedSides(): Int =
        sumOf { point ->
            point.neighours().count { it !in this }
        }
}
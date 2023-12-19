package aoc23

import aoc23.Day11Domain.Observatory
import aoc23.Day11Parser.toObservatory
import com.github.shiguruikai.combinatoricskt.combinations
import common.Space2D
import common.Space2D.Parser.toPointToChars
import common.Year23

object Day11 : Year23 {
    fun List<String>.part1(): Long =
        toObservatory(emptySpaceMultiplier = 2)
            .sumOfShortestPaths()

    fun List<String>.part2(emptySpaceMultiplier: Int): Long =
        toObservatory(emptySpaceMultiplier = emptySpaceMultiplier)
            .sumOfShortestPaths()
}

object Day11Domain {
    data class Observatory(
        private val galaxyMap: Map<Space2D.Point, Char>,
        private val emptySpaceMultiplier: Int
    ) {
        fun sumOfShortestPaths(): Long =
            expandGalaxies()
                .toShortestPaths()
                .sum()

        private fun expandGalaxies(): Set<Space2D.Point> =
            galaxyMap
                .toGalaxies()
                .expandGalaxies(
                    expandUpFromY = galaxyMap.emptyAlong { it.y },
                    expandRightFromX = galaxyMap.emptyAlong { it.x },
                    emptySpaceMultiplier = emptySpaceMultiplier,
                )
    }

    private fun Map<Space2D.Point, Char>.emptyAlong(function: (Space2D.Point) -> Int): Set<Int> =
        keys
            .groupBy(function)
            .filterValues { points -> points.all { this[it] == '.' } }
            .keys

    private fun Map<Space2D.Point, Char>.toGalaxies(): Set<Space2D.Point> =
        this
            .filterValues { it != '.' }
            .keys

    private fun Set<Space2D.Point>.expandGalaxies(
        expandUpFromY: Set<Int>,
        expandRightFromX: Set<Int>,
        emptySpaceMultiplier: Int
    ): Set<Space2D.Point> =
        this.map { point ->
            point
                .move(
                    direction = Space2D.Direction.North,
                    by = (expandUpFromY.count { it < point.y } * (emptySpaceMultiplier - 1))
                )
                .move(
                    direction = Space2D.Direction.East,
                    by = (expandRightFromX.count { it < point.x } * (emptySpaceMultiplier - 1))
                )
        }.toSet()

    private fun Set<Space2D.Point>.toShortestPaths(): List<Long> =
        this.combinations(2)
            .map { it.first().distanceTo(it.last()).toLong() }
            .toList()

}

object Day11Parser {
    fun List<String>.toObservatory(emptySpaceMultiplier: Int): Observatory =
        Observatory(
            galaxyMap = toPointToChars().toMap(),
            emptySpaceMultiplier = emptySpaceMultiplier
        )
}

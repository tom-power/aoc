package aoc23

import common.Misc.log
import common.Year23
import aoc23.Day14Domain.ParabolicReflectorDish
import aoc23.Day14Parser.toParabolicReflectorDish
import common.Monitoring
import common.Space2D.Direction
import common.Space2D.Parser.toPointToChars
import common.Space2D.Point
import common.Space2D.toLoggable

object Day14 : Year23 {
    fun List<String>.part1(): Int =
        toParabolicReflectorDish()
            .apply { tiltNorth() }
            .calculateLoad()

    fun List<String>.part2(): Int =
        toParabolicReflectorDish().log()
            .let { 0 }
}

object Day14Domain {
    data class ParabolicReflectorDish(
        val rockSpaceMap: MutableMap<Point, Char>
    ) {
        fun tiltNorth() {
            rockSpaceMap
                .toSortedMap()
                .reversed()
                .forEach { (point, rockSpace) ->
                    if (rockSpace == 'O') {
                        var next: Point? = moveRock(point, Direction.Up)
                        while(next != null)
                            next = moveRock(next, Direction.Up)
                    }
                }
        }

        private fun moveRock(point: Point, direction: Direction): Point? =
            point.move(direction)
                .takeIf { next -> rockSpaceMap[next] == '.' }
                ?.also { next ->
                    with(rockSpaceMap) {
                        next.also {
                            this[point] = '.'
                            this[next] = 'O'
                        }
                    }
                }

        fun calculateLoad(): Int =
            rockSpaceMap
                .filterValues { it == 'O' }
                .map { (point, _) -> point.y }
                .sum()

    }
}

object Day14Parser {
    fun List<String>.toParabolicReflectorDish(): ParabolicReflectorDish =
        ParabolicReflectorDish(
            rockSpaceMap = toPointToChars().toMap().toMutableMap()
        )
}

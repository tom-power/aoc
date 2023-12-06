package aoc22

import aoc22.Day14Domain.Cave
import aoc22.Day14Parser.toCave
import aoc22.Day14Solution.part1Day14
import aoc22.Day14Solution.part2Day14
import common.Space2D.Direction.*
import common.Space2D.Point
import common.Year22
import common.Monitoring

object Day14: Year22 {
    fun List<String>.part1(): Int = part1Day14()

    fun List<String>.part2(): Int = part2Day14()
}

object Day14Solution {
    fun List<String>.part1Day14(): Int =
        toCave()
            .fillUpAndCount()

    fun List<String>.part2Day14(): Int =
        toCave()
            .apply {
                rock.addAll(floor(belowLowest = 2))
                lowestRock = rock.minBy { it.y }
            }
            .fillUpAndCount()
}

object Day14Domain {
    data class Cave(
        val rock: MutableSet<Point>,
        val sandStartsFrom: Point,
        var fallingSand: Point,
        var lowestRock: Point = rock.minBy { it.y },
        var last: Point = lowestRock,
        private val monitor: Monitoring.PointMonitor? = null
    ) {
        private var atRest: Int = 0

        fun hasNext(): Boolean =
            fallingSand.isAbove(lowestRock) &&
                last != sandStartsFrom

        fun fillUpAndCount(): Int {
            while (hasNext()) {
                fallingSand.movesBelow().firstOrNull { it !in rock }
                    .let { stillFallingSand ->
                        if (stillFallingSand != null) {
                            fallingSand = stillFallingSand
                        } else {
                            rock.add(fallingSand)
                            last = fallingSand
                            fallingSand = sandStartsFrom
                            atRest++
                        }
                    }
                monitor?.invoke(rock + fallingSand)
            }
            return atRest
        }

        fun floor(belowLowest: Int): List<Point> {
            val left: Int = rock.minOf { it.x } + lowestRock.y
            val right: Int = rock.maxOf { it.x } - lowestRock.y
            val down: Int = lowestRock.y - belowLowest
            return Point(x = left, y = down).lineTo(Point(x = right, y = down))
        }

        private fun Point.isAbove(other: Point): Boolean = this.y > other.y

        private fun Point.movesBelow() =
            listOf(
                move(Down),
                move(Down).move(Left),
                move(Down).move(Right)
            )
    }
}

object Day14Parser {
    fun List<String>.toCave(monitor: Monitoring.PointMonitor? = null): Cave =
        Cave(
            rock = this.flatMap { row -> row.toPoints().toLine() }.toMutableSet(),
            sandStartsFrom = Point(500, 0),
            fallingSand = Point(500, 0),
            monitor = monitor
        )

    private fun String.toPoints(): List<Point> =
        split(" -> ")
            .map {
                it.split(",")
                    .run { Point(this[0].toInt(), -this[1].toInt()) }
            }

    private fun List<Point>.toLine(): List<Point> =
        zipWithNext()
            .flatMap { (from, to) -> from.lineTo(to) }

}

package aoc22

import aoc22.Day12Domain.HeightMap
import aoc22.Day12Parser.toHeightMap
import aoc22.Day12Solution.part1Day12
import aoc22.Day12Solution.part2Day12
import aoc22.Space2D.Point
import aoc22.Space2D.Parser.parsePointChars
import java.util.*

object Day12 : Day {
    fun List<String>.part1(): Int = part1Day12()

    fun List<String>.part2(): Int = part2Day12()
}

object Day12Solution {
    fun List<String>.part1Day12(): Int =
        toHeightMap().run {
            shortestPath(
                begin = start,
                isDestination = { it == end },
                canMove = { from, to -> to - from <= 1 }
            )
        }

    fun List<String>.part2Day12(): Int =
        toHeightMap().run {
            shortestPath(
                begin = end,
                isDestination = { elevations[it] == 0 },
                canMove = { from, to -> from - to <= 1 }
            )
        }
}

object Day12Domain {

    class HeightMap(
        val elevations: Map<Point, Int>,
        val start: Point,
        val end: Point,
        private val visited: MutableSet<Point> = mutableSetOf(),
        private val pointQueue: PriorityQueue<PointCost> = PriorityQueue<PointCost>(),
        private val monitor: Monitoring.PointMonitor? = null
    ) {
        fun shortestPath(
            begin: Point,
            isDestination: (Point) -> Boolean,
            canMove: (Int, Int) -> Boolean
        ): Int {
            pointQueue.add(PointCost(begin, 0))

            while (pointQueue.isNotEmpty()) {
                val next = pointQueue.poll()
                val neighbors = neighbours(next.point, canMove)

                val nextCost = next.cost + 1

                if (neighbors.any { isDestination(it) })
                    return nextCost

                pointQueue.addAll(neighbors.map { PointCost(it, nextCost) })
                visited.addAll(neighbors)
                monitor?.invoke((visited + neighbors).toSet())
            }

            throw IllegalStateException("No valid path from $start to $end")
        }

        private fun neighbours(point: Point, canMove: (Int, Int) -> Boolean): List<Point> =
            point.adjacent()
                .filter { it !in visited }
                .filter { it in elevations }
                .filter { canMove(elevations.getValue(point), elevations.getValue(it)) }
    }

    data class PointCost(
        val point: Point,
        val cost: Int
    ) : Comparable<PointCost> {
        override fun compareTo(other: PointCost): Int =
            this.cost.compareTo(other.cost)
    }
}

object Day12Parser {
    private fun Char.toCode(): Int =
        when (this) {
            'S' -> 0
            'E' -> 25
            else -> this.code - 'a'.code
        }

    fun List<String>.toHeightMap(monitor: Monitoring.PointMonitor? = null): HeightMap =
        parsePointChars().toMap().let { p ->
            HeightMap(
                elevations = p.mapValues { it.value.toCode() },
                start = p.filterValues { it == 'S' }.keys.first(),
                end = p.filterValues { it == 'E' }.keys.first(),
                monitor = monitor
            )
        }
}

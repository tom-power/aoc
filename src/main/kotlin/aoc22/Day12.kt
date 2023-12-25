package aoc22

import aoc22.Day12Domain.HeightMap
import aoc22.Day12Parser.toHeightMap
import aoc22.Day12Solution.part1Day12
import aoc22.Day12Solution.part2Day12
import common.Monitoring
import common.Space2D.Point
import common.Space2D.Parser.toPointToChars
import common.Year22
import common.graph.Dijkstra
import common.graph.Edge
import common.graph.Node

object Day12 : Year22 {
    fun List<String>.part1(): Int = part1Day12()

    fun List<String>.part2(): Int = part2Day12()
}

object Day12Solution {
    fun List<String>.part1Day12(): Int =
        toHeightMap().run {
            shortestPath(
                begin = startPoint,
                isDestination = { it == endPoint },
                canMove = { from, to -> to - from <= 1 }
            )
        }

    fun List<String>.part2Day12(): Int =
        toHeightMap().run {
            shortestPath(
                begin = endPoint,
                isDestination = { elevations[it] == 0 },
                canMove = { from, to -> from - to <= 1 }
            )
        }
}

object Day12Domain {
    data class PointNode(
        override val value: Point
    ) : Node<Point>

    class HeightMap(
        val elevations: Map<Point, Int>,
        val startPoint: Point,
        val endPoint: Point,
        val monitor: Monitoring.PointMonitor? = null,
    ) {
        lateinit var begin: Point
        lateinit var isDestination: (Point) -> Boolean
        lateinit var canMove: (Int, Int) -> Boolean

        fun shortestPath(
            begin: Point,
            isDestination: (Point) -> Boolean,
            canMove: (Int, Int) -> Boolean
        ): Int {
            this.begin = begin
            this.isDestination = isDestination
            this.canMove = canMove
            return PointDijkstra()
                .run { shortestPath().also { monitor?.invoke(shortestPaths.last().first.map { it.value }.toSet()) } }
        }

        inner class PointDijkstra : Dijkstra<Point, PointNode>(monitoring = monitor != null) {
            override val start: () -> PointNode = { PointNode(value = begin) }
            override val isEnd: (PointNode) -> Boolean = { isDestination(it.value) }
            override fun next(node: PointNode): List<Edge<Point, PointNode>> =
                node.value.adjacent()
                    .filter { it in elevations }
                    .filter { canMove(elevations.getValue(node.value), elevations.getValue(it)) }
                    .map { Edge(PointNode(it), 1) }
        }
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
        toPointToChars().toMap().let { p ->
            HeightMap(
                elevations = p.mapValues { it.value.toCode() },
                startPoint = p.filterValues { it == 'S' }.keys.first(),
                endPoint = p.filterValues { it == 'E' }.keys.first(),
                monitor = monitor
            )
        }
}

package aoc23

import aoc23.Day17Domain.City
import aoc23.Day17Parser.toCity
import aoc23.Day17Solution.part2Day17
import common.Misc.log
import common.Monitoring
import common.Space2D
import common.Space2D.Direction
import common.Space2D.Direction.East
import common.Space2D.Parser.toPointToChars
import common.Space2D.Point
import common.Space2D.bottomRight
import common.Space2D.opposite
import common.Space2D.topLeft
import common.Year23
import common.graph.Dijkstra
import common.graph.Edge
import common.graph.Node

object Day17 : Year23 {
    fun List<String>.part1(): Int =
        toCity()
            .leastHeatLoss()

    fun List<String>.part2(): Int = part2Day17()
}

object Day17Solution {
    fun List<String>.part2Day17(): Int =
        toCity().log()
            .let { 0 }
}

object Day17Domain {
    data class City(
        val blockMap: Map<Point, Int>,
        val monitor: Monitoring.PointCharMonitor? = null
    ) {
        fun leastHeatLoss(): Int =
            BlockDijkstra().run {
                shortestPath()
                    .also {
                        monitor?.invoke(shortestPaths.last().first.map { it.value }.toSet())
                    }
            }

        data class BlockNode(
            override val value: Point,
            val directionCount: Int,
            val direction: Direction,
        ) : Node<Point>

        inner class BlockDijkstra : Dijkstra<Point, BlockNode>(monitoring = monitor != null) {
            private val topLeft = blockMap.keys.topLeft
            private val bottomRight = blockMap.keys.bottomRight

            override val start: () -> BlockNode =
                {
                    BlockNode(
                        value = topLeft,
                        directionCount = 0,
                        direction = East
                    )
                }

            override val isEnd: (BlockNode) -> Boolean = { it.value == bottomRight }

            override fun next(node: BlockNode): List<Edge<Point, BlockNode>> =
                with(node) {
                    Space2D.Direction.entries
                        .filter { nextDirection ->
                            nextDirection != direction.opposite() &&
                                (nextDirection != direction || directionCount < 3)
                        }
                        .map { value.move(it) to it }
                        .mapNotNull { (nextPoint, nextDirection) ->
                            blockMap[nextPoint]?.let { nextCost ->
                                Edge(
                                    node =
                                    BlockNode(
                                        value = nextPoint,
                                        directionCount =
                                        when {
                                            nextDirection == direction -> directionCount + 1
                                            else -> 1
                                        },
                                        direction = nextDirection,
                                    ),
                                    cost = nextCost
                                )
                            }
                        }
                }
        }
    }
}

object Day17Parser {
    fun List<String>.toCity(monitor: Monitoring.PointCharMonitor? = null): City =
        City(
            blockMap = this.toPointToChars().associate { it.first to it.second.digitToInt() },
            monitor = monitor,
        )
}

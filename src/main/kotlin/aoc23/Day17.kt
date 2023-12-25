package aoc23

import aoc23.Day17Domain.City
import aoc23.Day17Domain.City.BlockNode
import aoc23.Day17Parser.toCity
import aoc23.Day17Solution.fourToTenStepsDirectionPredicate
import aoc23.Day17Solution.threeStepsDirectionPredicate
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
            .leastHeatLoss(threeStepsDirectionPredicate)

    fun List<String>.part2(): Int =
        toCity()
            .leastHeatLoss(fourToTenStepsDirectionPredicate)
}

typealias DirectionPredicate = (BlockNode, Direction) -> Boolean

object Day17Solution {
    val threeStepsDirectionPredicate: DirectionPredicate = { node, nextDirection ->
        when {
            node.directionCount > 2 -> nextDirection != node.direction
            else -> true
        }
    }

    val fourToTenStepsDirectionPredicate: DirectionPredicate = { node, nextDirection ->
        when {
            node.directionCount < 4 -> nextDirection == node.direction
            node.directionCount > 9 -> nextDirection != node.direction
            else -> true
        }
    }
}

object Day17Domain {
    data class City(
        val blockMap: Map<Point, Int>,
        val monitor: Monitoring.PointCharMonitor? = null
    ) {
        fun leastHeatLoss(directionPredicate: DirectionPredicate): Int =
            BlockDijkstra(directionPredicate).run {
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

        inner class BlockDijkstra(
            private val directionPredicate: DirectionPredicate
        ) : Dijkstra<Point, BlockNode>(monitoring = monitor != null) {
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
                        .filter { nextDirection -> nextDirection != direction.opposite() }
                        .filter { nextDirection -> directionPredicate(node, nextDirection) }
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

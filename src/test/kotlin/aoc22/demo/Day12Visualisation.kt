package aoc22.demo

import aoc22.Day12
import aoc22.Day12Parser.toHeightMap
import common.Monitoring
import common.Space2D
import visualisation.animate
import common.Input.readInputExample
import common.Space2D.toEdges

fun day12Visualisation() {
    framesFor(input = Day12.readInputExample())
        .animate(100)
}


private fun framesFor(input: List<String>): List<String> =
    Monitoring.PointMonitor(canvas = forFive().toEdges().toSet())
        .also { monitor ->
            input.toHeightMap(monitor = monitor).run {
                shortestPath(
                    begin = start,
                    isDestination = { it == end },
                    canMove = { from, to -> to - from <= 1 }
                )
            }
        }.toLoggable()

private fun forFive() = listOf(Space2D.Point(-1, 6), Space2D.Point(8, 0))
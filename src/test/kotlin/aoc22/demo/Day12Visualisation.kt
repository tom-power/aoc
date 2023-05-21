package aoc22.demo

import aoc22.Day12
import aoc22.Day12Parser.toHeightMap
import aoc22.Monitoring
import aoc22.Space2D
import aoc22.visualisation.animate
import aoc22.visualisation.toFrame
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

fun day12Visualisation() {
    framesFor(input = Day12.readInputExample())
        .animate(100)
}


private fun framesFor(input: List<String>): List<String> =
    Monitoring.PointMonitor(frame = forFive().toFrame().toSet())
        .also { monitor ->
            input.toHeightMap(monitor = monitor).run {
                shortestPath(
                    begin = start,
                    isDestination = { it == end },
                    canMove = { from, to -> to - from <= 1 }
                )
            }
        }.print()

private fun forFive() = listOf(Space2D.Point(-1, 6), Space2D.Point(8, 0))
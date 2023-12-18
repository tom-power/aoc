package aoc22.demo

import aoc22.Day09
import aoc22.Day09Solution
import common.Monitoring
import common.Space2D
import visualisation.animate
import common.Input.readInputExample
import common.Space2D.toEdges

fun day09Visualisation() {
    framesFor(input = Day09.readInputExample())
        .animate()
}

private fun framesFor(input: List<String>): List<String> =
    Monitoring.PointMonitor(canvas = forFive().toEdges().toSet())
        .also { monitor ->
            with(Day09Solution) { input.toDirections().toHistory(knots = 2, monitor = monitor) }
        }.toLoggable()

private fun forFive() = listOf(Space2D.Point(-5, -5), Space2D.Point(5, 5))
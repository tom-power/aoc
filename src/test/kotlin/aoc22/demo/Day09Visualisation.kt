package aoc22.demo

import aoc22.Day09
import aoc22.Day09Solution
import common.Monitoring
import common.Space2D
import aoc22.visualisation.animate
import aoc22.visualisation.toFrame
import common.Input.readInputExample

fun day09Visualisation() {
    framesFor(input = Day09.readInputExample())
        .animate()
}


private fun framesFor(input: List<String>): List<String> =
    Monitoring.PointMonitor(frame = forFive().toFrame().toSet())
        .also { monitor ->
            with(Day09Solution) { input.toDirections().toHistory(knots = 2, monitor = monitor) }
        }.print()

private fun forFive() = listOf(Space2D.Point(-5, -5), Space2D.Point(5, 5))
package aoc22.demo

import aoc22.Day09
import aoc22.Day09Solution
import aoc22.Monitoring
import aoc22.Space2D
import aoc22.visualisation.animate
import aoc22.visualisation.toFrame
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

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
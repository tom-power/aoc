package aoc22.demo

import aoc22.Day24
import aoc22.Day24Monitoring.ValleyLocationMonitor
import aoc22.Day24Parser.toValley
import visualisation.animate
import common.Input.readInput

fun day24Visualisation() {
    val input = Day24.readInput("_example")
    framesFor(input = input).animate(frameDuration = 500)
}

private fun framesFor(input: List<String>): List<String> =
    ValleyLocationMonitor()
        .apply {
            input.toValley(monitor = this).run {
                shortestPath(
                    begin = start,
                    isDestination = { this == end },
                )
            }
        }.toLoggable()


package aoc22.demo

import aoc22.Day14
import aoc22.Day14Parser
import common.Monitoring
import visualisation.animate
import common.Input.readInputExample
import common.Space2D.toEdges

fun day14Visualisation() {
    framesFor(input = Day14.readInputExample())
        .animate()
}

private fun framesFor(input: List<String>): List<String> {
    val cave = with(Day14Parser) { input.toCave() }
    val frame = cave.rock + cave.sandStartsFrom
    return Monitoring.PointMonitor(canvas = frame.toEdges().toSet())
        .also { monitor ->
            with(Day14Parser) {
                cave.copy(monitor = monitor).fillUpAndCount()
            }
        }.toLoggable()
}
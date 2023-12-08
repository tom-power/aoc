package aoc22.demo

import aoc22.Day14
import aoc22.Day14Parser
import common.Monitoring
import aoc22.visualisation.animate
import aoc22.visualisation.toFrame
import common.Input.readInputExample

fun day14Visualisation() {
    framesFor(input = Day14.readInputExample())
        .animate()
}

private fun framesFor(input: List<String>): List<String> {
    val cave = with(Day14Parser) { input.toCave() }
    val frame = cave.rock + cave.sandStartsFrom
    return Monitoring.PointMonitor(frame = frame.toFrame().toSet())
        .also { monitor ->
            with(Day14Parser) {
                cave.copy(monitor = monitor).fillUpAndCount()
            }
        }.print()
}
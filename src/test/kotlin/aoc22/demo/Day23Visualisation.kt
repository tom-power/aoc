package aoc22.demo

import aoc22.Day23
import aoc22.Day23Parser.toElves
import aoc22.Day23Runner.SpreadOut
import common.Monitoring.PointMonitor
import common.Space2D.Point
import visualisation.animate
import common.Input.readInputExample
import common.Space2D.toEdges

fun day23Visualisation() {
    framesFor(input = Day23.readInputExample(), rounds = 10)
        //            .freezeAt(10)
        .animate()
}

private fun framesFor(input: List<String>, rounds: Int, frame: Collection<Point> = emptySet()): List<String> =
    PointMonitor(canvas = frame.toEdges().toSet())
        .apply {
            SpreadOut(
                elves = input.toElves(),
                monitor = this
            )
                .until(rounds)
        }.toLoggable()
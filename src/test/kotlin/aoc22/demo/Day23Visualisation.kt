package aoc22.demo

import aoc22.Day23
import aoc22.Day23Parser.toElves
import aoc22.Day23Runner.SpreadOut
import aoc22.Monitoring.PointMonitor
import aoc22.Space2D.Point
import aoc22.visualisation.animate
import aoc22.visualisation.toFrame
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

fun day23Visualisation() {
    framesFor(input = Day23.readInputExample(), rounds = 10)
        //            .freezeAt(10)
        .animate()
}

private fun framesFor(input: List<String>, rounds: Int, frame: Collection<Point> = emptySet()): List<String> =
    PointMonitor(frame = frame.toFrame().toSet())
        .apply {
            SpreadOut(
                elves = input.toElves(),
                monitor = this
            )
                .until(rounds)
        }.print()
package aoc22

import aoc22.Day23Parser.toElves
import aoc22.Day23Runner.SpreadOut
import common.Monitoring.PointMonitor
import common.Space2D.Point
import visualisation.animate
import common.Input.readInput
import common.Space2D.toEdges
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("visualisation")
class Day23VisualisationTest {
    @Test
    fun `part one 5 elves visualisation`() {
        framesFor(input = Day23.readInput("_exampleFiveElves"), rounds = 4, frame = forFive())
//            .freezeAt(4)
            .animate()
    }
}

private fun forFive() = listOf(Point(-5, -5), Point(10, 10))

private fun framesFor(input: List<String>, rounds: Int, frame: Collection<Point> = emptySet()): List<String> =
    PointMonitor(canvas = frame.toEdges().toSet())
        .apply {
            SpreadOut(
                elves = input.toElves(),
                monitor = this
            )
                .until(rounds)
        }.toLoggable()
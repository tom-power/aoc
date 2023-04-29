package aoc22

import aoc22.Day23Parser.toElves
import aoc22.Day23Runner.SpreadOut
import aoc22.Space2D.Point
import aoc22.Space2D.toMaxPoints
import aoc22.visualisation.animate
import aoc22.visualisation.freezeAt
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day23VisualisationTest {


    @Test
    @Disabled
    fun `part one 5 elves visualisation`() {
        framesFor(input = Day23.readInputFor("exampleFiveElves"), rounds = 4, frame = forFive())
//            .freezeAt(4)
            .animate()
    }

    @Test
    @Disabled
    fun `part one example visualisation`() {
        framesFor(input = Day23.readInputExample(), rounds = 10)
//            .freezeAt(10)
            .animate()

    }

    @Test
    @Disabled
    fun `part one visualisation`() {
        framesFor(input = Day23.readInput(), rounds = 10)
//            .freezeAt(1)
            .animate()
    }
}


private fun forFive() = listOf(Point(-5, -5), Point(10, 10))

private fun Collection<Point>.toFrame(): Collection<Point> {
    val minX = this.minOfOrNull { it.x }
    val maxX = this.maxOfOrNull { it.x }
    val minY = this.minOfOrNull { it.y }
    val maxY = this.maxOfOrNull { it.y }
    return toMaxPoints().filter { it.x == minX || it.x == maxX || it.y == minY || it.y == maxY}
}

private fun framesFor(input: List<String>, rounds: Int, frame: Collection<Point> = emptySet()): List<String> =
    ElfMonitor(frame = frame.toFrame().toSet())
        .apply {
            SpreadOut(
                elves = input.toElves(),
                monitor = this
            )
                .until(rounds)
        }.print()
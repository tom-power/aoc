package aoc23

import aoc23.Day14Parser.toParabolicReflectorDish
import common.Input.readInputExample
import common.Monitoring.PointMonitor
import common.Space2D
import common.Space2D.Parser.toPointChars
import common.Space2D.toEdges
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import visualisation.animate

@Tag("visualisation")
class Day14VisualisationTest {
    @Test
    fun `part one visualisation up`() {
        framesFor(input = Day14.readInputExample(), cycles = 1, direction = Space2D.Direction.Up)
            .animate(frameDuration = 1000)
    }

    @Test
    fun `part one visualisation left`() {
        framesFor(input = Day14.readInputExample(), cycles = 1, direction = Space2D.Direction.Left)
            .animate(frameDuration = 1000)
    }

    @Test
    fun `part one visualisation down`() {
        framesFor(input = Day14.readInputExample(), cycles = 1, direction = Space2D.Direction.Down)
            .animate(frameDuration = 1000)
    }

    @Test
    fun `part one visualisation right`() {
        framesFor(input = Day14.readInputExample(), cycles = 1, direction = Space2D.Direction.Right)
            .animate(frameDuration = 1000)
    }

    @Test
    fun `part two visualisation 1`() {
        framesFor(input = Day14.readInputExample(), cycles = 1, direction = null)
            .animate()
    }

    @Test
    fun `part two visualisation 2`() {
        framesFor(input = Day14.readInputExample(), cycles = 2, direction = null)
            .animate()
    }

    @Test
    fun `part two visualisation 3`() {
        framesFor(input = Day14.readInputExample(), cycles = 3, direction = null)
            .animate()
    }
}

private fun framesFor(input: List<String>, cycles: Int, direction: Space2D.Direction?): List<String> {
    val toPointChars = input.toPointChars()
    val squares = toPointChars.filter { it.char == '#' }.map { it.point }.toSet()
    val edges = toPointChars.map { it.point }.toEdges().toSet()

    return PointMonitor(
        canvas = squares,
        edges = edges,
        highlightWith = "O",
        showEdge = false
    )
        .apply {
            input.toParabolicReflectorDish(maxSpinCycles = cycles, monitor = this).let { dish ->
                direction
                    ?.let { dish.tiltTo(it) }
                    ?: dish.spinCycles()
            }
        }
        .toLoggable()
}
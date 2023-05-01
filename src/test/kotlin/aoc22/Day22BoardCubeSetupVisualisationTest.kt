package aoc22

import aoc22.Day22Parser.toBoard
import aoc22.Day22DomainWrapCube.EdgePointMap
import aoc22.Day22DomainWrapCube.EdgePoints
import aoc22.Day22Parser.toPoints
import aoc22.visualisation.animate
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("visualisation")
class Day22BoardCubeSetupVisualisationTest {

    @Test
    fun `can make CubeWrap mappings visualisation for example (real one is too big)`() {
        framesFor(Day22.readInputExample())
            .animate()
    }
}

private fun framesFor(input: List<String>): List<String> {
    val edgePoints = EdgePoints(input.toBoard().items.toPoints()).invoke()
    val monitor = EdgePointMapMonitor(edges = edgePoints.map { it.point }.toSet())
    val map = EdgePointMap(edgePoints = edgePoints, monitor = monitor)
    return map.apply { invoke() }.monitor!!.print()
}

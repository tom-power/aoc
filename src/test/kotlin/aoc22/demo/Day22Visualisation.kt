package aoc22.demo

import aoc22.Day22
import aoc22.Day22Parser.toBoard
import aoc22.Day22DomainWrapCube.EdgePointMap
import aoc22.Day22DomainWrapCube.EdgePoints
import aoc22.Day22Parser.toPoints
import aoc22.EdgePointMapMonitor
import aoc22.visualisation.animate
import common.readInputExample
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

fun day22Visualisation() {
    framesFor(Day22.readInputExample())
        .animate()
}

private fun framesFor(input: List<String>): List<String> {
    val edgePoints = EdgePoints(input.toBoard().items.toPoints()).invoke()
    val monitor = EdgePointMapMonitor(edges = edgePoints.map { it.point }.toSet())
    val map = EdgePointMap(edgePoints = edgePoints, monitor = monitor)
    return map.apply { invoke() }.monitor!!.print()
}

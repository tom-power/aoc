package aoc22

import aoc22.Day22Domain.State
import aoc22.Day22Domain.Step
import aoc22.Day22Domain.Tile
import aoc22.Day22Domain.Wall
import common.Misc.log
import common.Monitoring.Monitor
import common.Space2D
import common.Space2D.Direction.*
import common.Space2D.Point
import common.Space2D.print
import common.Space2D.toRange

class EdgePointMapMonitor(private val edges: Set<Point> = emptySet()) : Monitor<Set<EdgePointPair>> {

    private val edgePointMaps: MutableList<Set<EdgePointPair>> = mutableListOf()

    override fun invoke(p1: Set<EdgePointPair>) {
        edgePointMaps.add(p1)
    }

    override fun print(): List<String> =
        edgePointMaps
            .map { pairs -> edges.print(pairs.flatMap { listOf(it.first.point, it.second.point) }) }
}

class StateMonitor(
    private val items: Set<Day22Domain.BoardItem>,
    private var states: MutableList<State> = mutableListOf(),
    private var steps: MutableList<State> = mutableListOf()
) : (State, Step) -> State {
    override fun invoke(state: State, step: Step): State =
        state
            .also { states.add(it); steps.add(it) }

    fun print(highlight: Point? = null) {
        items.map { it.point }.print(highlight).log()
    }

    private fun Collection<Point>.print(highlight: Point? = null): String =
        Space2D.Axis.Y.toRange().reversed().map { y ->
            Space2D.Axis.X.toRange().map { x ->
                val point = Point(x, y)
                (states.lastOrNull { it.point == point && point == highlight }
                    ?: items.lastOrNull { it.point == point && point == highlight })?.let { "*" }
                    ?: states.lastOrNull { it.point == point }
                        ?.let {
                            when (it.facing) {
                                Right -> ">"
                                Down -> "v"
                                Left -> "<"
                                Up -> "^"
                            }
                        }
                    ?: items.lastOrNull { it.point == point }?.let {
                        when (it) {
                            is Tile -> "."
                            is Wall -> "#"
                        }
                    }
                    ?: " "
            }.joinToString("")
        }.joinToString(System.lineSeparator())
}
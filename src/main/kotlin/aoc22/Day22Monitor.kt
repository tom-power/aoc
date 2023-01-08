package aoc22

import aoc22.Day22Domain.State
import aoc22.Day22Domain.Step
import aoc22.Day22Domain.Tile
import aoc22.Day22Domain.Wall
import aoc22.Misc.log
import aoc22.Space2D.Direction.*
import aoc22.Space2D.Point
import aoc22.Space2D.toRange

class Monitor(
    private val items: Set<Day22Domain.BoardItem>,
    private var states: MutableList<State> = mutableListOf(),
    private var steps: MutableList<State> = mutableListOf()
) : (State, Step) -> State {
//    init {
//        print()
//    }

    override fun invoke(state: State, step: Step): State =
        state
            .also { states.add(it); steps.add(it) }
//            .also { println(state); println(step) }

    fun print(highlight: Point? = null) {
//        states.map { it.log() }
//        print("steps: ${steps.count()}, states: ${states.count()}, last: ${states.last()}")
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
package aoc22

import aoc22.Day09Solution.part1Day09
import aoc22.Day09Solution.part2Day09
import aoc22.Matrix.Direction
import aoc22.Matrix.Point
import kotlin.math.sign

object Day09 : Day {
    fun List<String>.part1(): Int = part1Day09()

    fun List<String>.part2(): Int = part2Day09()
}

object Day09Solution {
    fun List<String>.part1Day09(): Int = toDirections().toHistory(knots = 2).tailHistory.size

    fun List<String>.part2Day09(): Int = toDirections().toHistory(knots = 10).tailHistory.size

    data class State(
        val head: Point,
        val rope: List<Point>,
        val tailHistory: Set<Point>
    )

    private fun List<Direction>.toHistory(knots: Int): State {
        val initialState = State(Point(0, 0), listOf(Point(0, 0)), emptySet())

        return fold(initialState) { acc, direction ->

            val movedHead = acc.head.move(direction)

            fun List<Point>.addTail(): List<Point> = if (this.size < knots - 1) this + initialState.rope else this

            val rope = acc.rope.addTail().withMovements(movedHead)

            State(
                head = movedHead,
                rope = rope,
                tailHistory = acc.tailHistory + rope.last()
            )
        }
    }

    private fun List<Point>.withMovements(movedHead: Point): List<Point> {
        val movedPoints = mutableListOf<Point>()
        return mapIndexed { index, point ->
            movedPoints.getOrElse(index - 1) { movedHead }
                .let { prev ->
                    val movedPoint =
                        if (point !in listOf(prev) + prev.getAdjacentWithDiagonal())
                            pointNearest(point, prev)
                        else
                            point
                    movedPoint.also { movedPoints.add(it) }
                }
        }
    }

    private fun pointNearest(point: Point, other: Point) =
        Point(
            x = point.x + (other.x - point.x).sign,
            y = point.y + (other.y - point.y).sign
        )

    private fun List<String>.toDirections(): List<Direction> =
        map {
            it.split(" ")
                .let { (dir, times) -> Direction.valueOf(dir) to (0 until times.toInt()) }
                .let { (dir, times) -> times.map { dir } }
        }
            .flatten()
}

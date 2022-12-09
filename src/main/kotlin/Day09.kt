package aoc22

import aoc22.Day09Solution.part1Day09
import aoc22.Day09Solution.part2Day09
import aoc22.Matrix.Direction
import aoc22.Matrix.Point
import aoc22.Matrix.distanceBetween
import aoc22.Matrix.hasMatchingAxis
import aoc22.Matrix.move
import aoc22.Misc.log

object Day09 : Day<String, Int> {
    override fun List<String>.part1(): Int = part1Day09()

    override fun List<String>.part2(): Int = part2Day09()
}

object Day09Solution {
    data class State(
        val head: Point,
        val tail: Point,
        val tailHistory: Set<Point>
    )

    fun List<String>.part1Day09(): Int {
        val initialState = State(Point(0, 0), Point(0, 0), emptySet())

        return toDirections()
            .fold(initialState) { acc, direction ->
                val movedHead = acc.head.move(direction)
                val tail = if (distanceBetweenWithDiagonalOffset(acc.tail, movedHead) > 1) acc.head else acc.tail
                State(
                    head = movedHead,
                    tail = tail,
                    tailHistory = acc.tailHistory + tail
                )
            }.tailHistory.size
    }

    private fun distanceBetweenWithDiagonalOffset(point: Point, other: Point): Int =
        distanceBetween(point, other) - (if (hasMatchingAxis(point, other)) 0 else 1)

    private fun List<String>.toDirections(): List<Direction> =
        map {
            it.split(" ")
                .let { (dir, times) ->
                    (0 until times.toInt()).map { Direction.valueOf(dir) }
                }
        }
            .flatten()

    fun List<String>.part2Day09(): Int =
        map { it.split(",") }.log()
            .let { 0 }
}

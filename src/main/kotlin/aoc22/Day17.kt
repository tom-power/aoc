package aoc22

import aoc22.Day17Domain.Shape
import aoc22.Day17Domain.Shapes.backwardsEl
import aoc22.Day17Domain.Shapes.longBar
import aoc22.Day17Domain.Shapes.plus
import aoc22.Day17Domain.Shapes.square
import aoc22.Day17Domain.Shapes.wideBar
import aoc22.Day17Parser.toJets
import aoc22.Day17Runner.Cave
import aoc22.Day17Solution.part1Day17
import aoc22.Day17Solution.part2Day17
import aoc22.Matrix.Direction
import aoc22.Matrix.Direction.*
import aoc22.Matrix.HasPoints
import aoc22.Matrix.Point
import aoc22.Matrix.height

object Day17 : Day {
    fun List<String>.part1(): Int = part1Day17()

    fun List<String>.part2(): Long = part2Day17()
}

object Day17Solution {
    fun List<String>.part1Day17(): Int =
        Cave(
            jets = this.toJets(),
            cycles = 2022
        )
            .apply { dropShapes() }
            .rocks.height()

    fun List<String>.part2Day17(): Long =
        Cave(
            jets = this.toJets(),
            cycles = 1000000000000
        )
            .apply { dropShapes() }
            .rocks.height().toLong()
}

object Day17Runner {
    class Cave(
        private val jets: List<Direction>,
        private val cycles: Long
    ) {
        private val shapes = listOf(wideBar, plus, backwardsEl, longBar, square)
        private val width = 0..6
        val rocks: MutableList<Point> = width.map { Point(it, 0) }.toMutableList()

        private var cycle = 0
        private var jetsCycle = 0

        private fun Shape.moveToStart(): Shape = move(Up, by = rocks.height() + 4).move(Right, by = 2)
        private fun Shape.isInAir(): Boolean = rocks.takeLast(200).intersect(points).isEmpty()
        private fun Shape.isInChamber(): Boolean = points.map { it.x }.all { it in width }
        private fun Shape.moveIfValid(direction: Direction): Shape =
            move(direction).takeIf { it.isInChamber() && it.isInAir() } ?: this

        fun dropShapes() {
            while (cycle < cycles) {
                var shape = shapes.nth(cycle++).moveToStart()
                while (shape.isInAir()) {
                    shape =
                        shape
                            .moveIfValid(direction = jets.nth(jetsCycle++))
                            .move(Down)
                }
                rocks.addAll(shape.move(Up).points)
            }
        }

        private fun <T> List<T>.nth(n: Int): T = this[n % size]
    }
}

object Day17Domain {
    data class Shape(
        override val points: Set<Point>
    ) : HasPoints {
        override fun move(direction: Direction, by: Int): Shape = copy(points = points.move(direction, by).toSet())
    }

    object Shapes {
        /**
         * ####
         */
        val wideBar = Shape(
            setOf(
                Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)
            )
        )

        /**
         * .#.
         * ###
         * .#.
         */
        val plus = Shape(
            setOf(
                Point(1, 2),
                Point(0, 1), Point(1, 1), Point(2, 1),
                Point(1, 0),
            )
        )

        /**
         * ..#
         * ..#
         * ###
         */
        val backwardsEl = Shape(
            setOf(
                Point(2, 2),
                Point(2, 1),
                Point(0, 0), Point(1, 0), Point(2, 0),
            )
        )

        /**
         * #
         * #
         * #
         * #
         */
        val longBar = Shape(
            setOf(
                Point(0, 3),
                Point(0, 2),
                Point(0, 1),
                Point(0, 0),
            )
        )

        /**
         * ##
         * ##
         */
        val square = Shape(
            setOf(
                Point(0, 1), Point(1, 1),
                Point(0, 0), Point(1, 0),
            )
        )
    }
}

object Day17Parser {
    fun List<String>.toJets(): List<Direction> =
        this[0].map {
            when (it) {
                '>' -> Right
                '<' -> Left
                else -> error("direction $it not found")
            }
        }
}

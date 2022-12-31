package aoc22

import aoc22.Day17Domain.Shapes.backwardsEl
import aoc22.Day17Domain.Shapes.longBar
import aoc22.Day17Domain.Shapes.plus
import aoc22.Day17Domain.Shapes.square
import aoc22.Day17Domain.Shapes.wideBar
import aoc22.Day17Parser.toJets
import aoc22.Day17Domain.Chamber
import aoc22.Day17Runner.DropWithSimulation
import aoc22.Day17Runner.DropWithSimulation.DropValues
import aoc22.Day17Runner.LoopSimulator.Simulated
import aoc22.Day17Solution.part1Day17
import aoc22.Day17Solution.part2Day17
import aoc22.Matrix.Direction
import aoc22.Matrix.Direction.*
import aoc22.Matrix.HasPoints
import aoc22.Matrix.Point
import aoc22.Matrix.height

object Day17 : Day {
    fun List<String>.part1(): Long = part1Day17()

    fun List<String>.part2(): Long = part2Day17()
}

object Day17Solution {
    fun List<String>.part1Day17(): Long =
        DropWithSimulation(
            chamber = Chamber(jets = this.toJets()),
            maxDrops = 2022
        ).invoke()

    fun List<String>.part2Day17(): Long =
        DropWithSimulation(
            chamber = Chamber(jets = this.toJets()),
            maxDrops = 1000000000000
        ).invoke()
}

object Day17Runner {
    data class Loop(
        val start: DropValues,
        val end: DropValues,
    ) {
        val drops = (end.drop - start.drop)
        val height = (end.floorHeight - start.floorHeight)
    }

    class LoopSimulator(
        private val loop: Loop,
        private val maxDrops: Long,
    ): () -> Simulated {
        data class Simulated(val dropsRemaining: Long, val height: Long)

        override fun invoke(): Simulated {
            val loopsRemaining: Long = (maxDrops - loop.end.drop) / loop.drops

            return Simulated(
                dropsRemaining = maxDrops - loop.end.drop - (loopsRemaining * loop.drops),
                height = loop.height * loopsRemaining
            )
        }
    }

    class DropWithSimulation(
        private val chamber: Chamber,
        private val maxDrops: Long,
    ) : () -> Long {
        private var simulatedHeight: Long = 0

        data class DropKey(val floorSurface: List<Int>, val dropMod: Int, val jetMod: Int)
        data class DropValues(val drop: Int, val floorHeight: Int)

        private val loopMap: MutableMap<DropKey, DropValues> = mutableMapOf()

        override fun invoke(): Long =
            chamber.apply {
                while (this.drop < maxDrops) {
                    dropNextShape()
                    // simulate if loop found
                    val currentDropKey = currentDropKey()
                    val currentDropValues = currentDropValues()
                    loopMap[currentDropKey]
                        ?.let { lastDropValues ->
                            LoopSimulator(
                                Loop(
                                    start = lastDropValues,
                                    end = currentDropValues,
                                ),
                                maxDrops = maxDrops
                            ).invoke().let { simulated ->
                                simulatedHeight = simulated.height
                                repeat(simulated.dropsRemaining.toInt()) {
                                    dropNextShape()
                                }
                                drop = maxDrops
                            }
                        }.ifNull {
                            loopMap[currentDropKey] = currentDropValues
                        }
                }
            }.floor.height() + simulatedHeight

        private fun currentDropValues() =
            DropValues(
                drop = chamber.drop.toInt(),
                floorHeight = chamber.floor.height()
            )

        private fun currentDropKey(): DropKey =
            chamber.run {
                DropKey(
                    floorSurface = floor.topRowHeights().normalised(),
                    dropMod = shapes.mod(drop.toInt()),
                    jetMod = jets.mod(jet)
                )
            }

        private fun List<Point>.topRowHeights(): List<Int> =
            groupBy { it.x }
                .entries
                .sortedBy { it.key }
                .map { column -> column.value.maxOf { point -> point.y } }

        private fun List<Int>.normalised(): List<Int> {
            val min = this.minOf { it }
            return this.map { it - min }
        }

        private fun <T> List<T>.mod(n: Int): Int = n % size
    }

}

object Day17Domain {
    class Chamber(
        val jets: List<Direction>,
    ) {
        val shapes = listOf(wideBar, plus, backwardsEl, longBar, square)
        private val width = 0..6
        val floor: MutableList<Point> = width.map { Point(it, 0) }.toMutableList()

        var drop: Long = 0
        var jet = 0

        private fun Shape.moveToStart(): Shape = move(Up, by = floor.height() + 4).move(Right, by = 2)
        private fun Shape.isInAir(): Boolean = floor.takeLast(200).intersect(points).isEmpty()
        private fun Shape.isInChamber(): Boolean = points.map { it.x }.all { it in width }
        private fun Shape.moveIfValid(direction: Direction): Shape =
            move(direction).takeIf { it.isInChamber() && it.isInAir() } ?: this

        fun dropNextShape() {
            var shape = shapes.nth((drop++).toInt()).moveToStart()
            while (shape.isInAir()) {
                shape =
                    shape
                        .moveIfValid(direction = jets.nth(jet++))
                        .move(Down)
            }
            floor.addAll(shape.move(Up).points)

        }

        private fun <T> List<T>.nth(n: Int): T = this[(n % size)]
    }

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

private fun <T> T?.ifNull(fn: () -> Unit) {
    if (this == null) {
        fn()
    }
}
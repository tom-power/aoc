package aoc23

import common.Year23
import aoc23.Day16Domain.Contraption
import aoc23.Day16Parser.toContraption
import common.Space2D
import common.Space2D.Direction.*
import common.Space2D.Parser.toPointToChars
import common.Space2D.Point
import common.Space2D.Side.Left
import common.Space2D.Side.Right
import common.Space2D.max
import common.Space2D.min
import common.Space2D.toEdges
import common.Space2D.turn
import kotlinx.coroutines.*

object Day16 : Year23 {
    fun List<String>.part1(): Int =
        toContraption()
            .apply { beamFromTopLeft() }
            .maxEnergizedCount()

    fun List<String>.part2(): Int =
        toContraption()
            .apply { beamsFromEverywhere() }
            .maxEnergizedCount()
}

object Day16Domain {
    data class PointDir(
        val point: Point,
        val direction: Space2D.Direction
    )

    data class Contraption(
        val grid: Map<Point, Char>
    ) {
        private val energizedCounts: MutableList<Int> = mutableListOf()
        private val topLeft
            get() = grid.keys.run {
                Point(
                    x = Space2D.Axis.X.min(),
                    y = Space2D.Axis.Y.max()
                )
            }
        private val terminalPoints = mutableListOf<Point>()

        fun maxEnergizedCount(): Int = energizedCounts.max()

        fun beamFromTopLeft() {
            RecordBeamsFrom(PointDir(topLeft, East)).invoke()
                .also(energizedCounts::add)
        }

        fun beamsFromEverywhere() {
            with(grid.keys) {
                toEdges().forEachParallel { startingPoint ->
                    if (startingPoint !in terminalPoints) {
                        RecordBeamsFrom(PointDir(startingPoint, startingPoint.toDirection())).invoke()
                            .also(energizedCounts::add)
                    }
                }
            }
        }

        private fun <T> Iterable<T>.forEachParallel(fn: suspend (T) -> Unit): Unit =
            runBlocking {
                forEach { async(Dispatchers.Default) { fn(it) } }
            }

        context(Collection<Point>)
        private fun Point.toDirection(): Space2D.Direction =
            when {
                this.x == Space2D.Axis.X.min() -> East
                this.x == Space2D.Axis.X.max() -> West
                this.y == Space2D.Axis.Y.min() -> North
                this.y == Space2D.Axis.Y.max() -> South
                else -> error("bad")
            }

        inner class RecordBeamsFrom(
            private val beamStart: PointDir
        ): () -> Int {
            private val energised: MutableList<PointDir> = mutableListOf()

            override fun invoke(): Int {
                energised.clear()
                nextBeam(beamStart, beamStart.point)
                return energizedCount
            }

            private val energizedCount get() = energised.map { it.point }.toSet().size

            private fun nextBeam(pointDir: PointDir, lastPoint: Point) {
                if (pointDir !in energised) {
                    grid[pointDir.point]?.let { char ->
                        energised.add(pointDir)

                        val nextSides =
                            encounterMap[char]!!
                                .filterKeys { pointDir.direction in it }
                                .values
                                .first()

                        when {
                            nextSides.isEmpty() -> listOf(pointDir.direction)
                            else -> nextSides.map { pointDir.direction.turn(it) }
                        }
                            .forEach { direction ->
                                nextBeam(
                                    pointDir = PointDir(
                                        point = pointDir.point.move(direction),
                                        direction = direction
                                    ),
                                    lastPoint = pointDir.point
                                )
                            }
                    } ?: terminalPoints.add(lastPoint)
                }
            }
        }

    }

    private val encounterMap: Map<Char, Map<List<Space2D.Direction>, List<Space2D.Side>>> =
        mapOf(
            '/' to mapOf(
                listOf(North, South) to listOf(Right),
                listOf(West, East) to listOf(Left),
            ),
            '\\' to mapOf(
                listOf(North, South) to listOf(Left),
                listOf(West, East) to listOf(Right),
            ),
            '-' to mapOf(
                listOf(North, South) to listOf(Left, Right),
                listOf(West, East) to listOf(),
            ),
            '|' to mapOf(
                listOf(North, South) to listOf(),
                listOf(West, East) to listOf(Left, Right),
            ),
            '.' to mapOf(
                listOf(North, South, West, East) to listOf(),
            ),
        )
}

object Day16Parser {
    fun List<String>.toContraption(): Contraption =
        Contraption(
            grid = toPointToChars().toMap()
        )
}

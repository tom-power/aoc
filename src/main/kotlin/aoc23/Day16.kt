package aoc23

import common.Year23
import aoc23.Day16Domain.Contraption
import aoc23.Day16Domain.PointDir
import aoc23.Day16Parser.toContraption
import common.Monitoring
import common.Space2D
import common.Space2D.Direction
import common.Space2D.Direction.*
import common.Space2D.Parser.toPointToChars
import common.Space2D.Point
import common.Space2D.Side
import common.Space2D.Side.Left
import common.Space2D.Side.Right
import common.Space2D.lastIn
import common.Space2D.max
import common.Space2D.min
import common.Space2D.nextIn
import common.Space2D.toEdges
import common.Space2D.turn
import kotlinx.coroutines.*

object Day16 : Year23 {
    fun List<String>.part1(): Int =
        toContraption()
            .apply { beamFromTopLeft() }
            .maxEnergized()

    fun List<String>.part2(): Int =
        toContraption()
            .apply { beamsFromEverywhere() }
            .maxEnergized()
}

object Day16Domain {
    class Contraption(
        private val tiles: Map<Point, Char>,
        private val monitor: Monitoring.PointCharMonitor? = null
    ) {
        private val beamVisitedPoints = mutableListOf<Set<Point>>()
        val terminalPoints = mutableListOf<Point>()

        fun maxEnergized(): Int = beamVisitedPoints.maxOf { it.size }

        fun beamFromTopLeft() {
            beamFrom(PointDir(tiles.keys.topLeft, East))
        }

        fun beamsFromEverywhere() {
            with(tiles.keys) {
                toEdges().forEachParallel { startingPoint ->
                    if (startingPoint !in terminalPoints) {
                        beamFrom(PointDir(startingPoint, startingPoint.toDirectionFromEdge()))
                    }
                }
            }
        }

        fun beamFrom(pointDir: PointDir) {
            RecordBeam(
                tiles = tiles,
                beamStart = pointDir,
                monitor = monitor
            )
                .apply { invoke() }
                .also { beam ->
                    beamVisitedPoints.add(beam.visited.map(PointDir::point).toSet())
                }
        }

    }

    data class PointDir(
        val point: Point,
        val direction: Direction
    )

    context(Contraption)
    @Suppress("ComplexRedundantLet")
    class RecordBeam(
        private val tiles: Map<Point, Char>,
        private val beamStart: PointDir,
        private val monitor: Monitoring.PointCharMonitor? = null,
    ) {
        val visited = mutableListOf<PointDir>()

        fun invoke() {
            visited.clear()
            visitTile(beamStart, beamStart)
        }

        private val pointsThatDivert = tiles.filterNot { it.value == '.' }.keys
        private val pointsAll = tiles.keys

        private fun visitTile(lastPointDir: PointDir, currentPointDir: PointDir) {
            val untilLast by lazy { lastPointDir.pointDirsUntil(currentPointDir.point) }

            fun nextPointThatDiverts(direction: Direction) =
                with(pointsThatDivert) { currentPointDir.point.nextIn(direction) }

            fun firstPointOutside(direction: Direction) =
                with(pointsAll) {
                    currentPointDir.point.lastIn(direction)
                        .move(direction = direction)
                }

            if (lastPointDir !in visited) {
                visited.addAll(untilLast)
                monitor?.invoke(visited.map { it.point }.toSet())

                when (val tile = tiles[currentPointDir.point]) {
                    null -> terminalPoints.add(untilLast.last().point)
                    else ->
                        currentPointDir.direction.run { toDirectionsFor(toSidesFor(tile)) }
                            .forEach { direction ->
                                (nextPointThatDiverts(direction) ?: firstPointOutside(direction))
                                    .let { nextPoint ->
                                        visitTile(
                                            lastPointDir = currentPointDir.copy(direction = direction),
                                            currentPointDir = PointDir(nextPoint, direction),
                                        )
                                    }
                            }
                }
            }
        }
    }
}

private fun Direction.toSidesFor(tile: Char): List<Side> =
    tileDiversionMap[tile]!!
        .filterKeys { directions -> this in directions }
        .values
        .first()

private fun Direction.toDirectionsFor(sides: List<Side>) =
    when {
        sides.isEmpty() -> listOf(this)
        else -> sides.map { side -> this.turn(side) }
    }

private fun PointDir.pointDirsUntil(nextPoint: Point): List<PointDir> =
    (0..<point.distanceTo(nextPoint))
        .map {
            PointDir(
                point = point.move(direction = direction, by = it),
                direction = direction
            )
        }

private fun <T> Iterable<T>.forEachParallel(fn: suspend (T) -> Unit): Unit =
    runBlocking {
        forEach { async(Dispatchers.Default) { fn(it) } }
    }

context(Collection<Point>)
private fun Point.toDirectionFromEdge(): Direction =
    when {
        this.x == Space2D.Axis.X.min() -> East
        this.x == Space2D.Axis.X.max() -> West
        this.y == Space2D.Axis.Y.min() -> North
        this.y == Space2D.Axis.Y.max() -> South
        else -> error("bad")
    }

private val Collection<Point>.topLeft: Point
    get() =
        Point(
            x = Space2D.Axis.X.min(),
            y = Space2D.Axis.Y.max()
        )

private val tileDiversionMap: Map<Char, Map<List<Direction>, List<Side>>> =
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

object Day16Parser {
    fun List<String>.toContraption(monitor: Monitoring.PointCharMonitor? = null): Contraption =
        Contraption(
            tiles = toPointToChars().toMap(),
            monitor = monitor
        )
}

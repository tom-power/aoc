package aoc22

import aoc22.Day15Domain.Sensor
import aoc22.Day15Parser.toSensors
import aoc22.Day15RunnerPart1.part1Day15
import aoc22.Day15RunnerPart2.part2Day15
import common.Space2D.Point
import common.Space2D.Parser.toPoint
import common.Day22
import kotlin.math.absoluteValue

object Day15: Day22() {
    fun List<String>.part1(y: Int): Int = part1Day15(y)

    fun List<String>.part2(gridMax: Int): Long = part2Day15(gridMax)
}

object Day15RunnerPart1 {
    fun List<String>.part1Day15(y: Int): Int =
        toSensors()
            .countOfNoBeaconAt(y = y)

    private fun List<Sensor>.countOfNoBeaconAt(y: Int): Int =
        map { sensor -> sensor.rangeOfXAt(y) }
            .flatMap { rangeOfX -> rangeOfX.toList() }
            .filterNot { x -> this.beaconAt(Point(x, y)) }
            .toSet()
            .count()

    private fun List<Sensor>.beaconAt(point: Point) =
        this.any { it.nearestBeacon.x == point.x && it.nearestBeacon.y == point.y }
}

object Day15RunnerPart2 {
    fun List<String>.part2Day15(gridMax: Int): Long =
        toSensors()
            .findBeacon(gridMax = gridMax)
            .toTuningFrequency()

    private fun List<Sensor>.findBeacon(gridMax: Int): Point {
        val gridRange = 0 until gridMax
        return toEdges()
            .filter { it.x in gridRange && it.y in gridRange }
            .first { !hasDetected(it) }
    }

    private fun List<Sensor>.hasDetected(maybeBeacon: Point): Boolean =
        any { sensor ->
            sensor.hasDetected(maybeBeacon)
        }

    private fun List<Sensor>.toEdges(): List<Point> = flatMap { it.edges() }

    fun Point.toTuningFrequency(): Long = (x * 4000000L) + y
}

object Day15Domain {
    data class Sensor(
        val location: Point,
        val nearestBeacon: Point,
    ) {
        private val distanceToBeacon = location.distanceTo(nearestBeacon)

        fun rangeOfXAt(y: Int, offset: Int = 0): IntRange {
            val distanceToY = (location.y - y).absoluteValue
            val maxRangeOfX = maxRangeFor(location.x).add(offset)

            return IntRange(
                start = maxRangeOfX.start + distanceToY,
                endInclusive = maxRangeOfX.endInclusive - distanceToY,
            )
        }

        private fun IntRange.add(offset: Int): IntRange {
            return IntRange(this.start - offset, this.endInclusive + offset)
        }

        private fun maxRangeFor(int: Int): IntRange =
            IntRange(
                start = int - distanceToBeacon,
                endInclusive = int + distanceToBeacon
            )

        fun edges(): List<Point> {
            val offset = 1
            val maxRangeForY = maxRangeFor(location.y).add(offset)
            val top = maxRangeForY.start
            val bottom = maxRangeForY.endInclusive
            return listOf(Point(location.x, top)) +
                (top until bottom)
                    .flatMap { y ->
                        rangeOfXAt(y, offset).run {
                            listOf(Point(start, y), Point(endInclusive, y))
                        }
                    } +
                listOf(Point(location.x, bottom))
        }

        fun hasDetected(maybeBeacon: Point): Boolean =
            location.distanceTo(maybeBeacon) <= distanceToBeacon

    }
}

object Day15Parser {
    fun List<String>.toSensors(): List<Sensor> =
        map { it.split(":").map { it.toPoint() } }
            .map { Sensor(it[0], it[1]) }
}

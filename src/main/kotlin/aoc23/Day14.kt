package aoc23

import common.Year23
import aoc23.Day14Domain.ParabolicReflectorDish
import aoc23.Day14Parser.toParabolicReflectorDish
import common.Monitoring
import common.Space2D.Direction
import common.Space2D.Direction.*
import common.Space2D.Parser.toPointToChars
import common.Space2D.Point
import java.util.*

object Day14 : Year23 {
    fun List<String>.part1(): Int =
        toParabolicReflectorDish()
            .apply { tiltTo(North) }
            .calculateLoad()

    fun List<String>.part2(): Int =
        toParabolicReflectorDish(maxSpinCycles = 1000000000)
            .apply { spinCycles() }
            .calculateLoad()
}

fun ParabolicReflectorDish.calculateLoad(): Int =
    rockSpaceMap
        .filterValues { it == 'O' }
        .map { (point, _) -> point.y }
        .sum()

object Day14Domain {
    data class ParabolicReflectorDish(
        val rockSpaceMap: MutableMap<Point, Char>,
        val maxSpinCycles: Int,
        val monitor: Monitoring.PointMonitor? = null,
    ) {
        fun tiltTo(direction: Direction) {
            rockSpaceMap
                .sortFor(direction = direction)
                .forEach { (point, rockSpace) ->
                    if (rockSpace == 'O') {
                        var next: Point? = moveRock(point, direction)
                        while (next != null)
                            next = moveRock(next, direction)
                    }
                }.also {
                    monitor?.invoke(rockSpaceMap.filterValues { it == 'O' }.keys)
                }
        }

        private fun moveRock(point: Point, direction: Direction): Point? =
            point.move(direction)
                .takeIf { next -> rockSpaceMap[next] == '.' }
                ?.also { next ->
                    with(rockSpaceMap) {
                        next.also {
                            this[point] = '.'
                            this[next] = 'O'
                        }
                    }
                }

        private var spinCycleNumber = 0
        private val pastStates: MutableList<String> = mutableListOf()
        private val currentState: String
            get() = rockSpaceMap.toString()

        fun spinCycles() {
            while (spinCycleNumber < maxSpinCycles) {
                pastStates.add(currentState)

                singleSpinCycle().also {
                    spinCycleNumber++
                    if (currentState in pastStates)
                        spinCycleNumber = lastCycleNumber()
                }
            }
        }

        private fun lastCycleNumber(): Int {
            val stateCycleLength = pastStates.size - pastStates.indexOf(currentState)
            val spinCyclesLeft = (maxSpinCycles - spinCycleNumber) % stateCycleLength
            return maxSpinCycles - spinCyclesLeft
        }

        private fun singleSpinCycle() {
            listOf(North, West, South, East).forEach(::tiltTo)
        }
    }
}

private fun MutableMap<Point, Char>.sortFor(direction: Direction): SortedMap<Point, Char> =
    when (direction) {
        East -> this.toSortedMap(rightComparator).reversed()
        South -> this.toSortedMap()
        West -> this.toSortedMap(rightComparator)
        North -> this.toSortedMap().reversed()
    }

private val rightComparator = Comparator<Point> { o1, o2 ->
    o1.x.compareTo(o2.x)
        .takeIf { it != 0 }
        ?: o1.y.compareTo(o2.y)
}

object Day14Parser {
    fun List<String>.toParabolicReflectorDish(
        maxSpinCycles: Int = 0,
        monitor: Monitoring.PointMonitor? = null
    ): ParabolicReflectorDish =
        ParabolicReflectorDish(
            rockSpaceMap = toPointToChars().toMap().toMutableMap(),
            maxSpinCycles = maxSpinCycles,
            monitor = monitor,
        )
}

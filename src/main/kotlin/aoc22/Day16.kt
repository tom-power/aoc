package aoc22

import aoc22.Day16Domain.Mountain
import aoc22.Day16Domain.Valve
import aoc22.Day16Domain.valvesToVisit
import aoc22.Day16Parser.toValves
import aoc22.Day16Solution.part1Day16
import aoc22.Day16Solution.part2Day16
import com.github.shiguruikai.combinatoricskt.combinations
import common.Year22

object Day16: Year22() {
    fun List<String>.part1(): Int = part1Day16()

    fun List<String>.part2(): Int = part2Day16()
}

object Day16Solution {
    fun List<String>.part1Day16(): Int =
        toValves()
            .associateBy { it.name }
            .let { Mountain(valveMap = it, minutes = 30).pressureReleased() }

    fun List<String>.part2Day16(): Int =
        toValves()
            .associateBy { it.name }
            .let {
                it.valvesToVisit()
                    .combinations(it.valvesToVisit().size / 2)
                    .maxOf { halfTheValvesToVisit ->
                        fun pressureReleasedFor(valvesToVisit: Set<String>) =
                            Mountain(
                                valveMap = it,
                                minutes = 26
                            ).pressureReleased(valvesToVisit)

                        listOf(
                            halfTheValvesToVisit.toSet(),
                            it.valvesToVisit() - halfTheValvesToVisit.toSet()
                        ).sumOf(::pressureReleasedFor)
                    }
            }
}

object Day16Domain {
    fun ValveMap.valvesToVisit() = filter { it.value.flowRate > 0 }.keys

    class Mountain(
        private val valveMap: Map<String, Valve>,
        private val minutes: Int,
    ) {
        fun pressureReleased(valvesToVisit: Set<String> = valveMap.valvesToVisit()): Int =
            PressureReleased(
                valveMap = valveMap,
                valvesToVisit = valvesToVisit,
                minutes = minutes,
                shortestDistanceMap = ShortestDistances(valveMap).invoke()
            ).invoke()
    }

    private class PressureReleased(
        private val valveMap: Map<String, Valve>,
        private val valvesToVisit: Set<String>,
        private val minutes: Int,
        private val shortestDistanceMap: ShortestDistanceMap
    ) : () -> Int {
        override fun invoke() = initialState().sumOfFlowsOverShortest()

        private fun initialState() =
            TraverseState(
                current = valveMap.getValue("AA"),
                remainingMinutes = minutes,
                remainingValves = valvesToVisit,
                finalFlow = 0,
            )

        private class TraverseState(
            val current: Valve,
            val remainingMinutes: Int,
            val remainingValves: Set<String>,
            val finalFlow: Int,
        )

        private fun TraverseState.sumOfFlowsOverShortest(): Int =
            currentFlow() +
                (remainingValves
                    .filter { next -> shortestDistanceTo(next) < remainingMinutes }
                    .map { next -> nextState(next).sumOfFlowsOverShortest() }
                    .maxOrNull()
                    ?: 0)

        private fun TraverseState.currentFlow() = remainingMinutes * current.flowRate
        private fun TraverseState.nextState(next: String) =
            TraverseState(
                current = valveMap.getValue(next),
                remainingMinutes = remainingMinutes - 1 - shortestDistanceTo(next),
                remainingValves = remainingValves - next,
                finalFlow = finalFlow
            )

        private fun TraverseState.shortestDistanceTo(other: String) = shortestDistanceMap[current.name]!![other]!!
    }

    private class ShortestDistances(
        private val valveMap: Map<String, Valve>,
    ) : () -> ShortestDistanceMap {
        override fun invoke(): ShortestDistanceMap = valveMap.keys.associateWith { shortestDistancesFrom(it) }

        private fun shortestDistancesFrom(valve: String): MutableMap<String, Int> {
            val shortestDistances =
                mutableMapOf<String, Int>()
                    .withDefault { Int.MAX_VALUE }
                    .apply { put(valve, 0) }

            fun visitNext(valve: String) {
                val nextDistance = shortestDistances[valve]!! + 1
                valveMap[valve]!!.otherValves
                    .forEach { nextValve ->
                        if (nextDistance < shortestDistances.getValue(nextValve)) {
                            shortestDistances[nextValve] = nextDistance
                            visitNext(nextValve)
                        }
                    }
            }

            visitNext(valve)
            return shortestDistances
        }
    }

    data class Valve(
        val name: String,
        val flowRate: Int,
        val otherValves: List<String>,
    )
}

private typealias ShortestDistanceMap = Map<String, Map<String, Int>>
private typealias ValveMap = Map<String, Valve>

object Day16Parser {
    fun List<String>.toValves(): List<Valve> =
        map { line ->
            line.replace("Valve ", "")
                .replace(" has flow rate=", ";")
                .replace(" tunnels lead to valves ", "")
                .replace(" tunnel leads to valve ", "")
                .split(";")
                .let {
                    Valve(
                        name = it[0],
                        flowRate = it[1].toInt(),
                        otherValves = it[2].split(", ")
                    )
                }

        }
}

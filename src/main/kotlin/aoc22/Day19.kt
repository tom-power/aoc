package aoc22

import common.Collections.product
import aoc22.Day19Domain.Blueprint
import aoc22.Day19Domain.Material.*
import aoc22.Day19Domain.RobotBlueprint
import aoc22.Day19Domain.State
import aoc22.Day19Parser.toBluePrints
import aoc22.Day19Runner.GeodeFactory
import aoc22.Day19Solution.part1Day19
import aoc22.Day19Solution.part2Day19
import common.Maps.minusInt
import common.Maps.plusInt
import common.Day22
import java.util.*
import kotlin.math.ceil

object Day19: Day22() {
    fun List<String>.part1(): Int = part1Day19()

    fun List<String>.part2(): Int = part2Day19()
}

object Day19Solution {
    fun List<String>.part1Day19(): Int =
        toBluePrints()
            .map { it.id * GeodeFactory(it, 24).maxGeodes() }
            .sum()

    fun List<String>.part2Day19(): Int =
        toBluePrints()
            .take(3)
            .map { GeodeFactory(it, 32).maxGeodes() }
            .product()
}

object Day19Runner {
    data class GeodeFactory(
        val blueprint: Blueprint,
        val maxTime: Int,
    ) {
        fun maxGeodes(): Int {
            var maxGeodes = 0

            val queue: PriorityQueue<State> = PriorityQueue(State.geodeComparator).apply { add(State.initial()) }

            while (queue.isNotEmpty()) {
                val state = queue.poll()

                if (canBeatMax(state, maxGeodes)) {
                    queue.addAll(nextStatesFor(state))
                }

                maxGeodes = maxOf(state.materialCount(Geode), maxGeodes)
            }

            return maxGeodes
        }

        private fun canBeatMax(state: State, maxGeodes: Int) = state.maxGeodesPossible(maxTime) > maxGeodes

        private fun nextStatesFor(state: State): List<State> =
            robotsToBuild(state)
                .filter { (_, timeNeeded) -> state.time + timeNeeded <= maxTime }
                .filter { (robotToBuild, _) -> robotToBuild.material == Geode || needMaterialFrom(robotToBuild, state) }
                .map { (robotToBuild, timeNeeded) ->
                    State(
                        time = state.time + timeNeeded,
                        materialCountMap = state.materialCountMap plusInt state.materialCollectedMap(timeNeeded) minusInt robotToBuild.costMap,
                        robotCountMap = state.robotCountMap plusInt robotToBuild.buildMap,
                    )
                }

        private val maxRobotCostMap = blueprint.maxRobotCostMap()

        private fun needMaterialFrom(robotBlueprint: RobotBlueprint, state: State) =
            with(robotBlueprint) {
                maxRobotCostMap[material]!! > state.robotCount(material)
            }

        private data class RobotPlan(val robotToBuild: RobotBlueprint, val timeNeeded: Int)

        private fun robotsToBuild(state: State): List<RobotPlan> =
            blueprint.robots
                .map { robotToBuild ->
                    RobotPlan(
                        robotToBuild = robotToBuild,
                        timeNeeded = robotToBuild.timeNeeded(state)
                    )
                }
    }

}

object Day19Domain {
    data class State(
        val time: Int,
        val materialCountMap: Map<Material, Int>,
        val robotCountMap: Map<Material, Int>
    ) {
        fun materialCount(material: Material) = materialCountMap.getOrDefault(material, 0)

        fun robotCount(material: Material) = robotCountMap.getOrDefault(material, 0)

        fun materialCollectedMap(timeNeeded: Int): Map<Material, Int> =
            robotCountMap.mapValues { it.value * timeNeeded }

        fun maxGeodesPossible(maxTime: Int): Int {
            val timeLeft = maxTime - time
            val mostPossible = (0 until timeLeft).sumOf { it + robotCount(Geode) }
            return materialCount(Geode) + mostPossible
        }

        companion object {
            fun initial(): State =
                State(
                    time = 1,
                    materialCountMap = mapOf(Ore to 1, Clay to 0, Obsidian to 0, Geode to 0),
                    robotCountMap = mapOf(Ore to 1, Clay to 0, Obsidian to 0, Geode to 0)
                )

            val geodeComparator = Comparator<State> { o1, o2 ->
                o2.materialCount(Geode).compareTo(o1.materialCount(Geode))
            }
        }
    }

    data class Blueprint(
        val id: Int,
        val robots: List<RobotBlueprint>,
    ) {
        fun maxRobotCostMap(): Map<Material, Int> =
            Material.values()
                .associateWith { material ->
                    this.robots.maxOf { robot -> robot.cost(material) }
                }
    }

    data class RobotBlueprint(
        val material: Material,
        val costMap: Map<Material, Int>,
    ) {

        fun timeNeeded(state: State): Int =
            this.costMap
                .maxOf { (material, _) -> this.timeToGet(material, state) } + 1

        private fun timeToGet(material: Material, state: State): Int =
            if (cost(material) <= state.materialCount(material)) 0 else timeToGetFuture(material, state)

        private fun timeToGetFuture(material: Material, state: State): Int {
            val materialNeeded = cost(material) - state.materialCount(material)
            return ceil(materialNeeded / state.robotCount(material).toFloat()).toInt()
        }

        val buildMap: Map<Material, Int> get() = mapOf(this.material to 1)

        fun cost(material: Material): Int = costMap.getOrDefault(material, 0)
    }

    enum class Material { Ore, Clay, Obsidian, Geode }
}

object Day19Parser {
    fun List<String>.toBluePrints(): List<Blueprint> =
        map {
            it.toRobotNumbers().let { numbers ->
                Blueprint(
                    id = numbers[0][0],
                    robots = listOf(
                        RobotBlueprint(
                            material = Ore,
                            costMap = mapOf(Ore to numbers[1][0]),
                        ),
                        RobotBlueprint(
                            material = Clay,
                            costMap = mapOf(Ore to numbers[2][0]),
                        ),
                        RobotBlueprint(
                            material = Obsidian,
                            costMap = mapOf(
                                Ore to numbers[3][0],
                                Clay to numbers[3][1]
                            ),
                        ),
                        RobotBlueprint(
                            material = Geode,
                            costMap = mapOf(
                                Ore to numbers[4][0],
                                Obsidian to numbers[4][1]
                            ),
                        ),
                    )
                )
            }
        }

    private fun String.toRobotNumbers(): List<List<Int>> =
        split(":", ".")
            .map { idAndRobots ->
                idAndRobots.split("ore")
                    .map { it.filter { it.isDigit() } }
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() }
            }
}

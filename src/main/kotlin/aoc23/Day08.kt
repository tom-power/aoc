package aoc23

import aoc23.Day08Domain.Network
import aoc23.Day08Parser.toNetwork
import aoc23.Day08Solution.part1Day08
import aoc23.Day08Solution.part2Day08
import common.Math.toLowestCommonMultiple
import common.Space3D.Parser.valuesFor
import common.Year23

object Day08 : Year23 {
    fun List<String>.part1(): Long = part1Day08()

    fun List<String>.part2(): Long = part2Day08()
}

object Day08Solution {
    fun List<String>.part1Day08(): Long =
        toNetwork()
            .toEndSingle()


    fun List<String>.part2Day08(): Long =
        toNetwork()
            .toEndParallel()
}

object Day08Domain {
    data class Network(
        val instructions: String,
        val nodeMap: Map<String, Pair<String, String>>,
    ) {
        private var steps: Long = 0L
        private var current = ""
        private var endFn: () -> Boolean = { false }

        fun toEndSingle(): Long =
            stepsToEndWith {
                steps = 0L
                current = "AAA"
                endFn = { current == "ZZZ" }
            }

        fun toEndParallel(): Long =
            startsWithA().map { start ->
                stepsToEndWith {
                    steps = 0L
                    current = start
                    endFn = { current.endsWith("Z") }
                }
            }.toLowestCommonMultiple()

        private fun startsWithA(): Set<String> = nodeMap.filter { it.key.endsWith("A") }.keys

        private fun stepsToEndWith(setup: () -> Unit): Long {
            setup()
            stepToEnd()
            return steps
        }

        private fun Network.stepToEnd() {
            while (!endFn()) {
                instructions.forEach { instruction ->
                    current = nodeMap[current]!!.nextFor(instruction)
                }
                steps += instructions.length
            }
        }

        private fun Pair<String, String>.nextFor(c: Char): String =
            when (c) {
                'L' -> first
                'R' -> second
                else -> error("bad instruction")
            }
    }
}

object Day08Parser {
    private val nodeRegex = Regex("([A-Z0-9]+) = \\(([A-Z0-9]+|), ([A-Z0-9]+)\\)")

    fun List<String>.toNetwork(): Network =
        Network(
            instructions = this.first().trim(),
            nodeMap = this.drop(2).associate { line ->
                val (name, left, right) = nodeRegex.valuesFor(line)
                name to Pair(left, right)
            }
        )

}

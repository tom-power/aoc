package aoc23

import aoc23.Day02Domain.Bag
import aoc23.Day02Domain.Cube
import aoc23.Day02Domain.CubeGame
import aoc23.Day02Domain.Round
import aoc23.Day02Parser.toCubeGame
import aoc23.Day02Solution.part1Day02
import aoc23.Day02Solution.part2Day02
import common.Collections.product
import common.Year23

object Day02 : Year23() {
    fun List<String>.part1(): Int = part1Day02()

    fun List<String>.part2(): Int = part2Day02()
}

object Day02Solution {
    private val part1Bag =
        Bag(
            cubeCount =
            mapOf(
                Cube.Red to 12,
                Cube.Green to 13,
                Cube.Blue to 14,
            )
        )

    fun List<String>.part1Day02(): Int =
        map { it.toCubeGame() }.filter { it.isPossibleWith(part1Bag) }
            .sumOf { it.id }

    fun List<String>.part2Day02(): Int =
        map { it.toCubeGame().smallestBagNeeded() }
            .sumOf { it.powerOfCubes() }
}

object Day02Domain {
    data class Bag(
        val cubeCount: Map<Cube, Int>
    ) {
        fun powerOfCubes(): Int =
            cubeCount.values.toList().product()
    }

    data class CubeGame(
        val id: Int,
        val rounds: List<Round>
    ) {
        fun isPossibleWith(bag: Bag): Boolean =
            bag.cubeCount.all { bagCubeCount ->
                rounds.all { round ->
                    (round.cubeCount[bagCubeCount.key] ?: 0) <= bagCubeCount.value
                }
            }

        fun smallestBagNeeded(): Bag =
            rounds.fold(Bag(mapOf())) { acc, round ->
                Bag(
                    cubeCount = mapOf(
                        Cube.Green to maxOf(acc, round, Cube.Green),
                        Cube.Red to maxOf(acc, round, Cube.Red),
                        Cube.Blue to maxOf(acc, round, Cube.Blue),
                    )
                )
            }

        private fun maxOf(bag: Bag, round: Round, cube: Cube): Int {
            val bagCount = bag.cubeCount[cube] ?: 0
            val roundCount = round.cubeCount[cube] ?: 0
            return maxOf(bagCount, roundCount)
        }
    }

    data class Round(
        val cubeCount: Map<Cube, Int>
    )

    enum class Cube {
        Green, Red, Blue
    }
}

object Day02Parser {
    // Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    fun String.toCubeGame(): CubeGame =
        this.split(":").let { game ->
            CubeGame(
                id = game.first().filter { it.isDigit() }.toInt(),
                rounds =
                game.last().split(";")
                    .map { round ->
                        Round(
                            cubeCount =
                            round.split(",")
                                .map { cubeCount ->
                                    val cube = cubeCount.filter { it.isLetter() }
                                    val count = cubeCount.filter { it.isDigit() }.toInt()
                                    Cube.valueOf(cube.capitalize()) to count
                                }.toMap()
                        )
                    }

            )
        }
}


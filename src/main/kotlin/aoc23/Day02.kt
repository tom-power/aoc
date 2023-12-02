package aoc23

import aoc23.Day02Domain.Bag
import aoc23.Day02Domain.Cube
import aoc23.Day02Domain.Cube.*
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
                Red to 12,
                Green to 13,
                Blue to 14,
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
    data class CubeGame(
        val id: Int,
        val rounds: List<Round>
    ) {
        fun isPossibleWith(bag: Bag): Boolean =
            rounds.all { round ->
                bag.hasEnoughCubesFor(round)
            }

        fun smallestBagNeeded(): Bag =
            Bag(
                cubeCount =
                Cube.entries
                    .associateWith { cube -> rounds.maxOf { it.countFor(cube) } }
            )
    }

    data class Bag(
        override val cubeCount: Map<Cube, Int>
    ) : HasCubeCount

    data class Round(
        override val cubeCount: Map<Cube, Int>
    ) : HasCubeCount

    interface HasCubeCount {
        val cubeCount: Map<Cube, Int>

        fun countFor(key: Cube): Int = (this.cubeCount[key] ?: 0)

        fun hasEnoughCubesFor(other: HasCubeCount): Boolean =
            cubeCount.all { (cube, count) ->
                count >= other.countFor(cube)
            }

        fun powerOfCubes(): Int = cubeCount.values.toList().product()
    }

    enum class Cube { Green, Red, Blue }
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
                                    val cubeName = cubeCount.filter { it.isLetter() }.capitalize()
                                    val count = cubeCount.filter { it.isDigit() }.toInt()
                                    Cube.valueOf(cubeName) to count
                                }.toMap()
                        )
                    }

            )
        }
}


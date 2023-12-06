package aoc23

import aoc23.Day03Parser.toSchema
import aoc23.Day03Solution.part1Day03
import aoc23.Day03Solution.part2Day03
import common.Collections.product
import common.Space2D
import common.Space2D.Parser.toPointChar
import common.Space2D.Point
import common.Space2D.PointChar
import common.Space2D.getFor
import common.Year23

object Day03 : Year23 {
    fun List<String>.part1(): Int = part1Day03()

    fun List<String>.part2(): Int = part2Day03()
}

object Day03Solution {
    fun List<String>.part1Day03(): Int =
        toSchema()
            .toPartNumbers()
            .sum()

    fun List<String>.part2Day03(): Int =
        toSchema()
            .toGearRatios()
            .sum()
}

object Day03Domain {
    data class Schematic(
        private val input: List<PointChar>
    ) {
        private val seen = mutableSetOf<PointChar>()
        private val numbersNextCharacters = mutableMapOf<List<Point>, Int>()
        private val charactersNextToNumbers = mutableSetOf<Point>()

        fun toGearRatios(): List<Int> {
            findCharactersAndNumbersThatIntersectWhere(characterPredicate = { isStar() })

            return charactersNextToNumbers
                .mapNotNull { characterPoint ->
                    numbersNextCharacters
                        .filter { (numberPoints, _) ->
                            numberPoints.intersect(characterPoint.adjacentWithDiagonal()).isNotEmpty()
                        }
                        .takeIf { it.size == 2 }
                        ?.map { it.value }
                }.map { it.product() }
        }

        fun toPartNumbers(): List<Int> {
            findCharactersAndNumbersThatIntersectWhere(characterPredicate = { isSymbol() })

            return numbersNextCharacters.map { it.value }
        }

        private fun findCharactersAndNumbersThatIntersectWhere(characterPredicate: PointChar.() -> Boolean) {
            input.forEach { pointChar ->
                when {
                    pointChar.isDigit() && pointChar !in seen -> {
                        getDigitsToRightOf(pointChar).also { seen.addAll(it) }
                            .let { numberPoints ->
                                numberPoints.pointsSurrounding().filter { it.characterPredicate() }.let { symbolNextToNumber ->
                                    if (symbolNextToNumber.isNotEmpty()) {
                                        numbersNextCharacters[numberPoints.map { it.point }] = numberPoints.toInt()
                                        charactersNextToNumbers.addAll(symbolNextToNumber.map { it.point })
                                    }
                                }
                            }
                    }
                }
            }
        }

        private fun getDigitsToRightOf(pointCharThatHasDigit: PointChar): List<PointChar> =
            generateSequence(pointCharThatHasDigit) { pointChar ->
                input.getFor(pointChar.point.move(Space2D.Direction.Right))?.takeIf { it.isDigit() }
            }.toList()

        private fun PointChar.isSymbol(): Boolean = !char.isDigit() && char != '.'
        private fun PointChar.isStar(): Boolean = char == '*'
        private fun PointChar.isDigit(): Boolean = char.isDigit()

        private fun List<PointChar>.pointsSurrounding(): Set<PointChar> =
            this.flatMap { it.point.adjacentWithDiagonal() }
                .mapNotNull { point -> input.getFor(point) }
                .toSet()

        private fun List<PointChar>.toInt() = map { it.char }.joinToString("").toInt()
    }
}

object Day03Parser {
    fun List<String>.toSchema(): Day03Domain.Schematic =
        Day03Domain.Schematic(input = toPointChar())
}
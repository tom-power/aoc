package aoc23

import aoc23.Day12Domain.SpringRecord
import aoc23.Day12Parser.toSpringRecords
import aoc23.Day12Parser.unfoldFiveTimes
import common.Strings.replaceFirst
import common.Year23

object Day12 : Year23 {
    fun List<String>.part1(): Long =
        toSpringRecords()
            .sumOf(SpringRecord::possibleArrangements)

    fun List<String>.part2(): Long =
        toSpringRecords(unfoldFn = unfoldFiveTimes)
            .sumOf(SpringRecord::possibleArrangements)
}

object Day12Domain {
    private const val replacements = "#."

    data class SpringRecord(
        val springs: String,
        val damagedGroupSizes: List<Int>
    ) {
        private val cache: MutableMap<Pair<String, List<Int>>, Long> = mutableMapOf()

        fun possibleArrangements(
            remainingSprings: String = springs,
            remainingDamagedGroupSizes: List<Int> = damagedGroupSizes,
        ): Long {
            val key = Pair(remainingSprings, remainingDamagedGroupSizes)

            cache[key]?.let { return it }

            when {
                remainingDamagedGroupSizes.isEmpty() -> {
                    return when {
                        remainingSprings.none { it == '#' } -> 1
                        else -> 0
                    }
                }

                remainingSprings.isEmpty() -> return 0
            }

            return when (remainingSprings.first()) {
                '.' ->
                    possibleArrangements(
                        remainingSprings.drop(1),
                        remainingDamagedGroupSizes,
                    )

                '?' ->
                    replacements.sumOf { replacement ->
                        possibleArrangements(
                            remainingSprings.replaceFirst('?', replacement),
                            remainingDamagedGroupSizes,
                        )
                    }

                '#' -> {
                    val thisMaybeDamagedSprings = remainingSprings.takeWhile { it != '.' }
                    val thisDamagedGroupSize = remainingDamagedGroupSizes.first()

                    when {
                        thisMaybeDamagedSprings.all { it == '#' } ->
                            when {
                                thisMaybeDamagedSprings.count() != thisDamagedGroupSize -> 0

                                thisMaybeDamagedSprings.count() == thisDamagedGroupSize ->
                                    possibleArrangements(
                                        remainingSprings.drop(thisDamagedGroupSize),
                                        remainingDamagedGroupSizes.drop(1),
                                    )

                                else -> error("not possible")
                            }

                        else ->
                            replacements.sumOf { replacement ->
                                possibleArrangements(
                                    remainingSprings.replaceFirst('?', replacement),
                                    remainingDamagedGroupSizes,
                                )
                            }
                    }
                }

                else -> error("unknown spring type")
            }.also { cache[key] = it }
        }
    }
}

object Day12Parser {
    fun List<String>.toSpringRecords(unfoldFn: String.() -> String = { this }): List<SpringRecord> =
        map { line ->
            line.unfoldFn().split(" ").let { split ->
                SpringRecord(
                    springs = split.first(),
                    damagedGroupSizes = split.last().split(",").map { it.toInt() }
                )
            }
        }

    val unfoldFiveTimes: String.() -> String = {
        this.split(" ").run {
            first().repeatFiveTimesWith('?') + " " + last().repeatFiveTimesWith(',')
        }
    }

    private fun String.repeatFiveTimesWith(separator: Char): String = (this + separator).repeat(5).dropLast(1)
}


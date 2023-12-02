package aoc22

import common.Day22

object Day03: Day22() {
    fun List<String>.part1(): Int = toCompartments().toSumOfCommon()

    fun List<String>.part2(): Int = toTrios().toSumOfCommon()

    private fun List<String>.toCompartments(): List<List<String>> =
        map { rucksackItems ->
            rucksackItems.chunked(rucksackItems.count() / 2)
        }

    private fun List<String>.toTrios(): List<List<String>> =
        chunked(3)

    private fun List<List<String>>.toSumOfCommon(): Int =
        map { it.commonItem() }
            .sumOf { it.toPriority() }

    private fun List<String>.commonItem(): Char =
        fold(get(0).toSet()) { acc, s ->
            s.toSet().intersect(acc.toSet())
        }.first()

    private fun Char.toPriority(): Int = code - asciiIndex() + upperCasePriority()

    private fun Char.asciiIndex(): Int = if (isUpperCase()) 64 else 96
    private fun Char.upperCasePriority(): Int = if (isUpperCase()) 26 else 0
}


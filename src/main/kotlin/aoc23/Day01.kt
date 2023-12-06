package aoc23

import aoc23.Day01Solution.part1Day01
import aoc23.Day01Solution.part2Day01
import common.Year23

object Day01 : Year23 {
    fun List<String>.part1(): Int = part1Day01()

    fun List<String>.part2(): Int = part2Day01()
}

object Day01Solution {
    fun List<String>.part1Day01(): Int =
        map { it.filterDigits() }
            .sumFirstAndLast()

    fun List<String>.part2Day01(): Int =
        map { it.firstDigit() + it.lastDigit() }
            .sumFirstAndLast()
}

private fun String.filterDigits(): String = this.filter { it.isDigit() }

private fun String.firstDigit(): String = findFirstDigit()

private fun String.lastDigit(): String = reversed().findFirstDigit { reversed() }

private fun String.findFirstDigit(fn: String.() -> String = { this }): String =
    this.fold("") { acc, c ->
        if (c.isDigit())
            return@findFirstDigit c.toString()

        (acc + c).let { current ->
            current.fn().firstWordDigit()
                ?.let { return@findFirstDigit it }
                ?: current
        }
    }

private fun String.firstWordDigit(): String? =
    wordsToDigits
        .firstOrNull { (word, _) -> this.contains(word) }
        ?.let { (_, digit) -> digit }

private val wordsToDigits: List<Pair<String, String>> =
    listOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

private fun List<String>.sumFirstAndLast(): Int =
    map { "${it.first()}${it.last()}" }
        .sumOf { it.toInt() }
package aoc23

import aoc23.Day01Solution.part1Day01
import aoc23.Day01Solution.part2Day01
import common.Year23

object Day01 : Year23() {
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

private fun String.firstDigit(): String =
    this.fold("") { acc, c ->
        if (c.isDigit())
            return@firstDigit c.toString()

        (acc + c).let { current ->
            current.firstWordToDigit()
                ?.let { return@firstDigit it }
                ?: current
        }
    }

private fun String.lastDigit(): String =
    this.foldRight("") { c, acc ->
        if (c.isDigit())
            return@lastDigit c.toString()

        (acc + c).let { current ->
            current.reversed().firstWordToDigit()
                ?.let { return@lastDigit it }
                ?: current
        }
    }

private fun String.firstWordToDigit(): String? =
    wordsToDigits.firstOrNull { this.contains(it.first) }?.second

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